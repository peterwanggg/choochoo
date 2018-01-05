package com.pwang.kings.stats;

import com.pwang.kings.objects.stats.ContestantRank;
import com.pwang.kings.objects.stats.ContestantStats;
import com.pwang.kings.objects.stats.ImmutableContestantRank;
import com.pwang.kings.objects.stats.RankType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author pwang on 1/4/18.
 */
public final class WinPercentRanker implements ContestantRanker {

    private final int minBouts;

    public WinPercentRanker(int minBouts) {
        this.minBouts = minBouts;
    }

    private static Double winPercentage(ContestantStats stats) {
        return (double) stats.getWinCount() / (double) (stats.getWinCount() + stats.getLoseCount());
    }

    @Override
    public List<ContestantRank> rankContestants(Collection<ContestantStats> contestantStats) {

        List<ContestantStats> sortedContestants = contestantStats.stream()
                .filter(stats -> stats.getLoseCount() + stats.getWinCount() >= minBouts)
                .sorted((s1, s2) -> {
                    if (s1.getWinCount() == 0 && s2.getWinCount() == 0) {
                        return s1.getLoseCount().compareTo(s2.getLoseCount());
                    } else if (s1.getWinCount() == 0) {
                        return 1;
                    } else if (s2.getWinCount() == 0) {
                        return -1;
                    } else {
                        Double s1WinP = winPercentage(s1);
                        Double s2WinP = winPercentage(s2);

                        Integer comp = s2WinP.compareTo(s1WinP);

                        if (comp == 0) {
                            return s2.getWinCount().compareTo(s1.getWinCount());
                        }
                        return comp;
                    }
                })
                .collect(Collectors.toList());

        return IntStream.rangeClosed(1, sortedContestants.size()).mapToObj(
                rank -> ImmutableContestantRank.builder()
                        .rank(rank)
                        .contestantId(sortedContestants.get(rank - 1).getContestantId())
                        .categoryId(sortedContestants.get(rank - 1).getCategoryId())
                        .rankType(RankType.winPercent)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public RankType getRankType() {
        return RankType.winPercent;
    }

}
