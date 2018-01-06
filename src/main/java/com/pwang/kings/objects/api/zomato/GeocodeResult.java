package com.pwang.kings.objects.api.zomato;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * @author pwang on 1/6/18.
 */
@JsonDeserialize(as = com.pwang.kings.objects.api.zomato.ImmutableGeocodeResult.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(as = com.pwang.kings.objects.api.zomato.ImmutableGeocodeResult.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface GeocodeResult extends ZomatoObject {

    @JsonProperty("location")
    Location location();
}
