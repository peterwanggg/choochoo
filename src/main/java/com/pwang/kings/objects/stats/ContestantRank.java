package com.pwang.kings.objects.stats;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * @author pwang on 1/4/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = com.pwang.kings.objects.stats.ImmutableContestantRank.class)
@JsonSerialize(as = com.pwang.kings.objects.stats.ImmutableContestantRank.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface ContestantRank {

    @JsonProperty
    Long getContestantId();

    @JsonProperty
    Long getCategoryId();

    @JsonProperty
    Integer getRank();

    @JsonProperty
    RankType getRankType();

}
