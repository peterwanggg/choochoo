package com.pwang.kings.objects.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author pwang on 12/27/17.
 */
public interface KingsObject extends Serializable {

    @JsonProperty
    String getApiProviderType();

    @JsonProperty
    String getApiProviderId();
}
