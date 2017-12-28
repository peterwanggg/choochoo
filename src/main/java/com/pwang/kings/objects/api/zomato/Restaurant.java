package com.pwang.kings.objects.api.zomato;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * @author pwang on 12/26/17.
 */

@JsonDeserialize(as = ImmutableRestaurant.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(as = ImmutableRestaurant.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface Restaurant extends ZomatoObject {

    @JsonProperty("restaurant")
    RestaurantValue getRestaurant();
}
