package com.pwang.kings.objects.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.Set;

/**
 * @author pwang on 1/18/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = com.pwang.kings.objects.model.ImmutableContestantSkips.class)
@JsonSerialize(as = com.pwang.kings.objects.model.ImmutableContestantSkips.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface ContestantSkips {

    @JsonProperty
    Long kingsUserId();

    @JsonProperty
    Long categoryId();

    @JsonProperty
    Set<Long> contestantIds();
}
