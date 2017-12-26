package com.pwang.clashbash;

import com.google.maps.GeoApiContext;
import com.google.maps.model.PlacesSearchResponse;
import com.pwang.clashbash.resources.ContestantsResource;
import com.pwang.serde.JsonParamConverterProvider;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author pwang on 12/25/17.
 */
public class ClashBashApplication extends Application<ClashBashConfiguration> {

    public static void main(String[] args) throws Exception {
        new ClashBashApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ClashBashConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor())
        );
    }

    @Override
    public void run(ClashBashConfiguration configuration, Environment environment) throws Exception {

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(configuration.getGoogleApiKey())
                .build();

//        environment.jersey().register(JsonParamConverterProvider.class);
        environment.jersey().register(PlacesSearchResponse.class);
        environment.jersey().register(new ContestantsResource(context));

    }
}
