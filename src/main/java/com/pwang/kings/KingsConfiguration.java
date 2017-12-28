package com.pwang.kings;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class KingsConfiguration extends Configuration {

    private String googleApiKey;
    @NotEmpty
    private String zomatoApiKey;

    @Valid
    @NotNull
    private DataSourceFactory database;


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

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }
}