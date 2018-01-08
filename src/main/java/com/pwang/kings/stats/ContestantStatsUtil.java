package com.pwang.kings.stats;

import com.pwang.kings.db.daos.BoutDao;
import com.pwang.kings.db.daos.ContestantRankDao;
import com.pwang.kings.db.daos.ContestantStatsDao;
import com.pwang.kings.objects.action.Bout;
import com.pwang.kings.objects.api.kings.ContestantEntry;
import com.pwang.kings.objects.api.kings.ContestantStatsApi;
import com.pwang.kings.objects.api.kings.ImmutableContestantEntry;
import com.pwang.kings.objects.api.kings.ImmutableContestantStatsApi;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.stats.ContestantRank;
import com.pwang.kings.objects.stats.ContestantStats;
import com.pwang.kings.objects.stats.ImmutableContestantStats;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static List<ContestantEntry> fetchAndJoinContestantStatsAndBouts(
            Long kingsUserId,
            List<Contestant> contestants,
            ContestantStatsDao contestantStatsDao,
            ContestantRankDao contestantRankDao,
            BoutDao boutDao) {

        if (contestants.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> contestantIds = contestants.stream().map(Contestant::getContestantId).collect(Collectors.toList());

        Map<Long, ContestantStats> statsMap = contestantStatsDao.getByIds(contestantIds)
                .stream()
                .collect(Collectors.toMap(
                        ContestantStats::getContestantId,
                        Function.identity()
                ));
        Map<Long, ContestantRank> rankMap = contestantRankDao.getByIds(contestantIds)
                .stream()
                .collect(Collectors.toMap(
                        ContestantRank::getContestantId,
                        Function.identity()
                ));
        Map<Long, List<Bout>> boutsMap = new HashMap<>();
        boutDao.getBoutsByUserAndContestantIds(kingsUserId, contestantIds)
                .stream()
                .forEach(bout -> {
                            List<Bout> winnersList = boutsMap.getOrDefault(bout.getWinnerContestantId(), new ArrayList<>());
                            winnersList.add(bout);
                            boutsMap.put(bout.getWinnerContestantId(), winnersList);
                            List<Bout> losersList = boutsMap.getOrDefault(bout.getLoserContestantId(), new ArrayList<>());
                            losersList.add(bout);
                            boutsMap.put(bout.getLoserContestantId(), losersList);
                        }
                );

        return contestants.stream()
                .map(contestant -> ImmutableContestantEntry.builder()
                        .contestant(contestant)
                        .contestantStats(fromDb(
                                statsMap.get(contestant.getContestantId()),
                                rankMap.get(contestant.getContestantId())))
                        .bouts(boutsMap.getOrDefault(contestant.getContestantId(), new ArrayList<>()))
                        .build())
                .collect(Collectors.toList());
    }

    public static ContestantStatsApi fromDb(ContestantStats dbStats, ContestantRank dbRank) {
        if (dbStats == null) {
            return ImmutableContestantStatsApi.builder().winCount(0).loseCount(0).build();
        }
        ImmutableContestantStatsApi.Builder builder = ImmutableContestantStatsApi.builder().winCount(dbStats.getWinCount()).loseCount(dbStats.getLoseCount());
        if (dbRank != null) {
            builder.putRanks(dbRank.getRankType(), dbRank.getRank());
        }
        return builder.build();
    }


}
