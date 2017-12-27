package com.pwang.kings.objects.api.zomato;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

/**
 * @author pwang on 12/27/17.
 */
@JsonDeserialize(as = com.pwang.kings.objects.api.zomato.ImmutableCitiesResult.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(as = com.pwang.kings.objects.api.zomato.ImmutableCitiesResult.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface CitiesResult extends ZomatoObject {

    @JsonProperty("location_suggestions")
    List<City> locationSuggestions();
}
