package com.pwang.kings.objects.api.kings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pwang.kings.objects.stats.RankType;
import org.immutables.value.Value;

import java.util.Map;

/**
 * @author pwang on 1/4/18.
 */
@JsonDeserialize(as = com.pwang.kings.objects.api.kings.ImmutableContestantStatsApi.class)
@JsonSerialize(as = com.pwang.kings.objects.api.kings.ImmutableContestantStatsApi.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface ContestantStatsApi {

    Integer winCount();

    Integer loseCount();

    Map<RankType, Integer> ranks();
}
