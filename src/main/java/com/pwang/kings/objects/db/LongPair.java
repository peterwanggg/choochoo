package com.pwang.kings.objects.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * @author pwang on 1/13/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = com.pwang.kings.objects.db.ImmutableLongPair.class)
@JsonSerialize(as = com.pwang.kings.objects.db.ImmutableLongPair.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface LongPair {

    @JsonProperty
    Long getLeft();

    @JsonProperty
    Long getRight();

}
