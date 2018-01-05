package com.pwang.kings.stats;

import com.google.common.collect.ImmutableList;
import com.pwang.kings.objects.stats.ContestantRank;
import com.pwang.kings.objects.stats.ContestantStats;
import com.pwang.kings.objects.stats.ImmutableContestantStats;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pwang on 1/4/18.
 */
public class WinPercentRankerTest {

    private WinPercentRanker ranker;

    @Before
    public void createRanker() {
        ranker = new WinPercentRanker(1);
    }

    @Test
    public void testSort() {

        List<ContestantStats> stats = ImmutableList.of(
                ImmutableContestantStats.builder().contestantId(1L).categoryId(12L)
                        .winCount(1).loseCount(1).build(),
                ImmutableContestantStats.builder().contestantId(2L).categoryId(12L)
                        .winCount(2).loseCount(2).build(),
                ImmutableContestantStats.builder().contestantId(3L).categoryId(12L)
                        .winCount(2).loseCount(1).build(),
                ImmutableContestantStats.builder().contestantId(4L).categoryId(12L)
                        .winCount(3).loseCount(1).build(),
                ImmutableContestantStats.builder().contestantId(5L).categoryId(12L)
                        .winCount(2).loseCount(0).build(),
                ImmutableContestantStats.builder().contestantId(6L).categoryId(12L)
                        .winCount(1).loseCount(0).build(),
                ImmutableContestantStats.builder().contestantId(7L).categoryId(12L)
                        .winCount(0).loseCount(0).build(),
                ImmutableContestantStats.builder().contestantId(8L).categoryId(12L)
                        .winCount(0).loseCount(2).build(),
                ImmutableContestantStats.builder().contestantId(9L).categoryId(12L)
                        .winCount(0).loseCount(1).build()
        );
        List<ContestantRank> ranks = ranker.rankContestants(stats);
        ranks.forEach(System.out::println);

        // 5, 6, 4, 3, 2, 1, 9, 8
        // removed: 7
        Long[] expectedIds = new Long[]{5L, 6L, 4L, 3L, 2L, 1L, 9L, 8L};
        Assert.assertArrayEquals(
                expectedIds,
                ranks.stream().map(ContestantRank::getContestantId).collect(Collectors.toList()).toArray(new Long[]{}));

        Integer[] expectedRanks = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8};
        Assert.assertArrayEquals(
                expectedRanks,
                ranks.stream().map(ContestantRank::getRank).collect(Collectors.toList()).toArray(new Integer[]{}));


    }


}
