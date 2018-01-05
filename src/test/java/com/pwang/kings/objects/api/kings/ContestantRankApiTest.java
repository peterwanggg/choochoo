package com.pwang.kings.objects.api.kings;

import com.pwang.kings.objects.stats.RankType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author pwang on 1/4/18.
 */
public class ContestantRankApiTest {

    @Test
    public void testNullFail() {
        assertThatThrownBy(() -> ImmutableContestantRankApi.builder().rankType(RankType.winPercent).build())
                .isInstanceOf(IllegalStateException.class);


    }
}
