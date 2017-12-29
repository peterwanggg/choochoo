package com.pwang.kings;

import com.google.maps.GeoApiContext;
import com.pwang.kings.auth.KingsAuthenticator;
import com.pwang.kings.auth.KingsAuthorizer;
import com.pwang.kings.categories.CategoryManagerFactory;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.db.daos.*;
import com.pwang.kings.db.util.JacksonMapperFactory;
import com.pwang.kings.objects.model.CategoryType;
import com.pwang.kings.objects.model.KingsUser;
import com.pwang.kings.resources.BoutResource;
import com.pwang.kings.resources.ContestantResource;
import com.pwang.kings.serde.ObjectMappers;
import com.pwang.kings.tasks.InitializeCategoryLocationTask;
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
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.skife.jdbi.v2.DBI;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


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
                return "migrations.sql";
            }

            @Override
            public DataSourceFactory getDataSourceFactory(KingsConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(KingsConfiguration configuration, Environment environment) throws Exception {

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

        // category manager
        CategoryManagerFactory categoryManagerFactory = new CategoryManagerFactory(
                zomatoService, locationDao, categoryDao, contestantDao);

        // environment setup
        environment.admin().addTask(new InitializeCategoryLocationTask(
                locationDao,
                CategoryType.restaurant,
                categoryManagerFactory.getCategoryManager(CategoryType.restaurant))
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
                boutDao
        ));
        environment.jersey().register(new ContestantResource(
                contestantDao,
                categoryDao,
                categoryManagerFactory));
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
