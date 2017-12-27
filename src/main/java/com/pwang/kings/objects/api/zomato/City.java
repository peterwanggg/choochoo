package com.pwang.kings.objects.api.zomato;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * @author pwang on 12/27/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = com.pwang.kings.objects.api.zomato.ImmutableCity.class)
@JsonSerialize(as = com.pwang.kings.objects.api.zomato.ImmutableCity.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface City extends ZomatoObject {
//    id (integer): ID of the city ,
//    locationName (string): City loca
//    country_id (integer, optional): ID of the country ,
//    country_name (string, optional): Name of the country ,
//    is_state (boolean, optional): Whether this location is a state ,
//    state_id (integer, optional): ID of the state ,
//    state_name (string, optional): Name of the state ,
//    state_code (string, optional): Short code for the state

    @JsonProperty("id")
    Integer id();

    @JsonProperty("name")
    String name();

    @JsonProperty("country_id")
    String countryId();

    @JsonProperty("country_name")
    Optional<String> countryName();


}
