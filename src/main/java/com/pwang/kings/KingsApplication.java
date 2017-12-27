package com.pwang.kings;

import com.google.maps.GeoApiContext;
import com.hubspot.rosetta.jdbi.RosettaMapperFactory;
import com.pwang.kings.categories.CategoryManagerFactory;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.db.daos.CategoryDao;
import com.pwang.kings.db.util.JacksonMapperFactory;
import com.pwang.kings.resources.ContestantResource;
import com.pwang.kings.serde.ObjectMappers;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.OptionalContainerFactory;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
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

//    private final HibernateBundle<HelloWorldConfiguration> hibernateBundle = new HibernateBundle<HelloWorldConfiguration>(Person.class) {
//        @Override
//        public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
//            return configuration.getDataSourceFactory();
//        }
//    };


    @Override
    public void initialize(Bootstrap<KingsConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor())
        );
    }

    @Override
    public void run(KingsConfiguration configuration, Environment environment) throws Exception {

        // api dependencies
        GeoApiContext googleContext = new GeoApiContext.Builder()
                .apiKey(configuration.getGoogleApiKey())
                .build();
        ZomatoService zomatoService = getZomatoService(configuration.getZomatoApiKey());

        // daos
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "jdbi");
        jdbi.registerContainerFactory(new OptionalContainerFactory());

//        jdbi.registerMapper(new RosettaMapperFactory());
        jdbi.registerMapper(new JacksonMapperFactory());
//        jdbi.register


        final CategoryDao categoryDao = jdbi.onDemand(CategoryDao.class);


        // environment setup
        environment.jersey().register(new JsonProcessingExceptionMapper(true));
        environment.jersey().register(new ContestantResource(zomatoService, categoryDao, new CategoryManagerFactory()));
//        environment.jersey().register(PlacesSearchResponse.class);
//        environment.jersey().register(LatLngConverterProvider.class);

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
                .addConverterFactory(JacksonConverterFactory.create(ObjectMappers.OBJECT_MAPPER))
                .build();

        return retrofit.create(ZomatoService.class);
    }

    private CategoryManagerFactory getCategoryManagerFactory() {
        return new CategoryManagerFactory();
    }
}
