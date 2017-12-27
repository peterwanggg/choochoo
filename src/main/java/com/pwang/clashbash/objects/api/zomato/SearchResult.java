package com.pwang.clashbash.objects.api.zomato;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

/**
 * @author pwang on 12/26/17.
 */
@JsonDeserialize(as = com.pwang.clashbash.objects.api.zomato.ImmutableSearchResult.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(as = com.pwang.clashbash.objects.api.zomato.ImmutableSearchResult.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface SearchResult {
    List<RestaurantResult> getRestaurants();
}
