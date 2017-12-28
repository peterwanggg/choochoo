package com.pwang.kings.objects.api.zomato;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * @author pwang on 12/27/17.
 */
@JsonDeserialize(as = com.pwang.kings.objects.api.zomato.ImmutableCuisineValue.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(as = com.pwang.kings.objects.api.zomato.ImmutableCuisineValue.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface CuisineValue extends ZomatoObject {

    @JsonProperty("cuisine_id")
    Integer cuisineId();

    @JsonProperty("cuisine_name")
    String cuisineName();

}
