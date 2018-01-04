package com.pwang.kings.objects.api.kings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.stats.ContestantStats;
import org.immutables.value.Value;

/**
 * @author pwang on 1/3/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = com.pwang.kings.objects.api.kings.ImmutableContestantEntry.class)
@JsonSerialize(as = com.pwang.kings.objects.api.kings.ImmutableContestantEntry.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface ContestantEntry {

    Contestant contestant();

    ContestantStats contestantStats();
}
