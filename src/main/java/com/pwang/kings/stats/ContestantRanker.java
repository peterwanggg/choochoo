package com.pwang.kings.stats;

import com.pwang.kings.objects.stats.ContestantRank;
import com.pwang.kings.objects.stats.ContestantStats;
import com.pwang.kings.objects.stats.RankType;

import java.util.Collection;
import java.util.List;

/**
 * @author pwang on 1/4/18.
 */
public interface ContestantRanker {

    List<ContestantRank> rankContestants(Collection<ContestantStats> contestantStats);

    RankType getRankType();

}
