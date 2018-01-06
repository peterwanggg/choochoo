package com.pwang.kings.objects.api.zomato;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * @author pwang on 1/6/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = com.pwang.kings.objects.api.zomato.ImmutableLocation.class)
@JsonSerialize(as = com.pwang.kings.objects.api.zomato.ImmutableLocation.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface Location extends ZomatoObject {

//  "location": {
//                "entity_type": "subzone",
//                "entity_id": 113080,
//                "title": "Capitol Hill",
//                "latitude": "47.6164307916",
//                "longitude": "-122.3200142662",
//                "city_id": 279,
//                "city_name": "Seattle",
//                "country_id": 216,
//                "country_name": "United States"
//    },


    @JsonProperty("entity_type")
    String entityType();

    @JsonProperty("entity_id")
    Integer entityId();

    @JsonProperty("title")
    String name();

    @JsonProperty("city_id")
    Optional<Integer> cityId();

    @JsonProperty("country_id")
    String countryId();

    @JsonProperty("country_name")
    Optional<String> countryName();


}
