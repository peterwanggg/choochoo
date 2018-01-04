package com.pwang.kings.stats;

import com.pwang.kings.db.daos.ContestantStatsDao;
import com.pwang.kings.objects.api.kings.ContestantEntry;
import com.pwang.kings.objects.api.kings.ImmutableContestantEntry;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.stats.ContestantStats;
import com.pwang.kings.objects.stats.ImmutableContestantStats;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author pwang on 1/3/18.
 */
public final class ContestantStatsUtil {

    private static final ImmutableContestantStats.Builder DEFAULT_CONTESTANT_STATS_BUILDER =
            ImmutableContestantStats.builder().loseCount(0).winCount(0);

    public static List<ContestantEntry> fetchAndJoinContestantStats(List<Contestant> contestants, ContestantStatsDao contestantStatsDao) {
        Map<Long, ContestantStats> statsMap = contestantStatsDao.getByIds(
                contestants.stream().map(Contestant::getContestantId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(
                        ContestantStats::getContestantId,
                        Function.identity()
                ));

        return contestants.stream()
                .map(contestant -> ImmutableContestantEntry.builder()
                        .contestant(contestant)
                        .contestantStats(statsMap.getOrDefault(
                                contestant.getContestantId(),
                                DEFAULT_CONTESTANT_STATS_BUILDER
                                        .contestantId(contestant.getContestantId())
                                        .categoryId(contestant.getCategoryId())
                                        .build()
                        ))
                        .build())
                .collect(Collectors.toList());
    }

}
