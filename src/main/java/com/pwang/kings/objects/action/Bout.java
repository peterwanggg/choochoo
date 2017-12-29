package com.pwang.kings.objects.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

/**
 * @author pwang on 12/28/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = com.pwang.kings.objects.action.ImmutableBout.class)
@JsonSerialize(as = com.pwang.kings.objects.action.ImmutableBout.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface Bout {

    @Nullable
    @JsonProperty
    Long getBoutId();

    @JsonProperty
    Long getCategoryId();

    @JsonProperty
    Long getWinnerContestantId();

    @JsonProperty
    Long getLoserContestantId();

    @JsonProperty
    Long getKingsUserId();
}

