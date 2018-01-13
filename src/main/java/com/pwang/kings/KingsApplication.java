package com.pwang.kings;

import com.google.maps.GeoApiContext;
import com.pwang.kings.auth.KingsAuthenticator;
import com.pwang.kings.auth.KingsAuthorizer;
import com.pwang.kings.categories.CategoryTypeManagerFactory;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.db.daos.*;
import com.pwang.kings.db.util.JacksonMapperFactory;
import com.pwang.kings.objects.model.CategoryType;
import com.pwang.kings.objects.model.KingsUser;
import com.pwang.kings.resources.*;
import com.pwang.kings.serde.ObjectMappers;
import com.pwang.kings.stats.WinPercentRanker;
import com.pwang.kings.tasks.ContestantRankerTask;
import com.pwang.kings.tasks.InitializeCategoryLocationTask;
import com.pwang.kings.tasks.ManagedPeriodicTask;
import com.pwang.kings.tasks.MaterializedViewRefreshTask;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.OptionalContainerFactory;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.Optional;


/**
 * @author pwang on 12/25/17.
 */
public class KingsApplication extends Application<KingsConfiguration> {

    public static void main(String[] args) throws Exception {
        new KingsApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<KingsConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor())
        );

        bootstrap.addBundle(new MigrationsBundle<KingsConfiguration>() {
            @Override
            public String getMigrationsFileName() {
                return "migrations/common.sql";
            }

            @Override
            public DataSourceFactory getDataSourceFactory(KingsConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(KingsConfiguration configuration, Environment environment) throws Exception {
        // TODO: remove potentically dicey cors things
        if (configuration.isInsecure()) {
            final FilterRegistration.Dynamic cors =
                    environment.servlets().addFilter("CORS", CrossOriginFilter.class);
            // Configure CORS parameters
            cors.setInitParameter("allowedOrigins", "*");
            cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin," +
                    "authorization"); // TODO: this part is especially dicey
            cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
            // Add URL mapping
            cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        }

        // api dependencies
        GeoApiContext googleContext = new GeoApiContext.Builder()
                .apiKey(configuration.getGoogleApiKey())
                .build();
        ZomatoService zomatoService = getZomatoService(configuration.getZomatoApiKey());

        // db setup
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "jdbi");
        jdbi.registerContainerFactory(new OptionalContainerFactory());
        jdbi.registerMapper(new JacksonMapperFactory());

        // daos
        final CategoryDao categoryDao = jdbi.onDemand(CategoryDao.class);
        final LocationDao locationDao = jdbi.onDemand(LocationDao.class);
        final ContestantDao contestantDao = jdbi.onDemand(ContestantDao.class);
        final BoutDao boutDao = jdbi.onDemand(BoutDao.class);
        final KingsUserDao kingsUserDao = jdbi.onDemand(KingsUserDao.class);
        final ContestantStatsDao contestantStatsDao = jdbi.onDemand(ContestantStatsDao.class);
        final ContestantRankDao contestantRankDao = jdbi.onDemand(ContestantRankDao.class);

        // category manager
        CategoryTypeManagerFactory categoryTypeManagerFactory = new CategoryTypeManagerFactory(
                zomatoService, locationDao, categoryDao, contestantDao, Optional.ofNullable(configuration.getCityIdOverride()));

        // environment setup
        environment.admin().addTask(new InitializeCategoryLocationTask(
                locationDao,
                CategoryType.restaurant,
                categoryTypeManagerFactory.getCategoryManager(CategoryType.restaurant))
        );
        environment.jersey().register(new JsonProcessingExceptionMapper(true));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(KingsUser.class));

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<KingsUser>()
                .setAuthenticator(new KingsAuthenticator(kingsUserDao))
                .setAuthorizer(new KingsAuthorizer())
                .setRealm("SUPER SECRET STUFF")
                .buildAuthFilter()));

        // register resources
        environment.jersey().register(new BoutResource(
                categoryTypeManagerFactory, boutDao, categoryDao, contestantStatsDao, contestantRankDao, contestantDao, locationDao));
        environment.jersey().register(new ContestantResource(
                contestantDao,
                categoryDao,
                categoryTypeManagerFactory,
                contestantStatsDao,
                contestantRankDao));
        environment.jersey().register(new CategoryResource(
                categoryTypeManagerFactory,
                contestantDao,
                contestantRankDao,
                contestantStatsDao));
        environment.jersey().register(new StatsResource(
                contestantStatsDao
        ));
        environment.jersey().register(new LocationResource(
                categoryTypeManagerFactory
        ));

        // set up background tasks
        final MaterializedViewRefreshTask materializedViewRefreshTask = new MaterializedViewRefreshTask(jdbi, configuration.getRefreshPeriodInSeconds());
        final Managed refreshImplementer = new ManagedPeriodicTask(materializedViewRefreshTask);
        environment.lifecycle().manage(refreshImplementer);

        final ContestantRankerTask contestantRankerTask = new ContestantRankerTask(
                categoryTypeManagerFactory,
                contestantStatsDao,
                contestantRankDao,
                locationDao,
                new WinPercentRanker(1),
                configuration.getRefreshPeriodInSeconds());
        final Managed rankImplementer = new ManagedPeriodicTask(contestantRankerTask);
        environment.lifecycle().manage(rankImplementer);
    }


    private ZomatoService getZomatoService(String apiKey) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        builder.addInterceptor(logging);
        builder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("user-key", apiKey).build();
            return chain.proceed(request);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com")
                .client(builder.build())
                .addConverterFactory(JacksonConverterFactory.create(ObjectMappers.RETROFIT_MAPPER))
                .build();

        return retrofit.create(ZomatoService.class);
    }

}
