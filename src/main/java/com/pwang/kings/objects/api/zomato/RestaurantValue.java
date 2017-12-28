package com.pwang.kings.objects.api.zomato;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * @author pwang on 12/26/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = ImmutableRestaurantValue.class)
@JsonSerialize(as = ImmutableRestaurantValue.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface RestaurantValue extends ZomatoObject {

    Integer getId();

    String getName();

    String getFeaturedImage();

}
