package com.pwang.kings.objects.api.kings;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author pwang on 1/4/18.
 */
public class ContestantStatsApiTest {
    @Test
    public void testEmptyList() {
        ContestantStatsApi obj = ImmutableContestantStatsApi.builder().winCount(0).loseCount(0).build();
        assertThat(obj.ranks()).isEmpty();

    }

    @Test
    public void testMissingFail() {
        assertThatThrownBy(() -> ImmutableContestantStatsApi.builder().loseCount(0).build())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testNullFail() {
        assertThatThrownBy(() -> ImmutableContestantStatsApi.builder().winCount(0).loseCount(0)
                .putRanks(null, 1).build())
                .isInstanceOf(NullPointerException.class);
    }
}
