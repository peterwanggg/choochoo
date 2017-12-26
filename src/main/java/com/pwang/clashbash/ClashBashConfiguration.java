package com.pwang.clashbash;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

//import com.pwang.helloworld.core.Template;

public class ClashBashConfiguration extends Configuration {


    private String googleApiKey;
    @JsonProperty
    public void setGoogleApiKey(String googleApiKey) {
        this.googleApiKey = googleApiKey;
    }
    @JsonProperty
    public String getGoogleApiKey() {
        return this.googleApiKey;
    }

}
