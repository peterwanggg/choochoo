package com.pwang.clashbash;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;


public class ClashBashConfiguration extends Configuration {

    private String googleApiKey;
    private String zomatoApiKey;


    @JsonProperty
    public void setGoogleApiKey(String googleApiKey) {
        this.googleApiKey = googleApiKey;
    }

    @JsonProperty
    public String getGoogleApiKey() {
        return this.googleApiKey;
    }

    @JsonProperty
    public String getZomatoApiKey() {
        return zomatoApiKey;
    }

    @JsonProperty
    public void setZomatoApiKey(String zomatoApiKey) {
        this.zomatoApiKey = zomatoApiKey;
    }
}
