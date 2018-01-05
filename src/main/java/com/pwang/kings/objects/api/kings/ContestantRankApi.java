package com.pwang.kings.objects.api.kings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pwang.kings.objects.stats.RankType;
import org.immutables.value.Value;

/**
 * @author pwang on 1/4/18.
 */
@JsonDeserialize(as = com.pwang.kings.objects.api.kings.ImmutableContestantRankApi.class)
@JsonSerialize(as = com.pwang.kings.objects.api.kings.ImmutableContestantRankApi.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface ContestantRankApi {

    RankType rankType();

    Integer rank();
}
