package com.pwang.kings;

import com.google.maps.GeoApiContext;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.resources.ContestantResource;
import com.pwang.kings.serde.LatLngConverterProvider;
import com.pwang.kings.serde.ObjectMappers;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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
    }

    @Override
    public void run(KingsConfiguration configuration, Environment environment) throws Exception {
        GeoApiContext googleContext = new GeoApiContext.Builder()
                .apiKey(configuration.getGoogleApiKey())
                .build();

        ZomatoService zomatoService = getZomatoService(configuration.getZomatoApiKey());


        environment.jersey().register(LatLngConverterProvider.class);
//        environment.jersey().register(PlacesSearchResponse.class);
        environment.jersey().register(new JsonProcessingExceptionMapper(true));
        environment.jersey().register(new ContestantResource(
                googleContext, zomatoService));

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
}
