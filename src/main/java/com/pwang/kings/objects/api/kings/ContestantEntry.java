package com.pwang.kings.objects.api.kings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pwang.kings.objects.action.Bout;
import com.pwang.kings.objects.model.Contestant;
import org.immutables.value.Value;

import java.util.List;

/**
 * @author pwang on 1/3/18.
 */
@JsonDeserialize(as = com.pwang.kings.objects.api.kings.ImmutableContestantEntry.class)
@JsonSerialize(as = com.pwang.kings.objects.api.kings.ImmutableContestantEntry.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface ContestantEntry {

    Contestant contestant();

    ContestantStatsApi contestantStats();

    List<Bout> bouts();
}
