package com.pwang.kings.resources;

import com.pwang.kings.api.StatsService;
import com.pwang.kings.db.daos.ContestantStatsDao;
import com.pwang.kings.objects.model.KingsUser;
import com.pwang.kings.objects.stats.ContestantStats;

import java.io.IOException;

/**
 * @author pwang on 1/3/18.
 */
public final class StatsResource implements StatsService {

    private final ContestantStatsDao contestantStatsDao;

    public StatsResource(ContestantStatsDao contestantStatsDao) {
        this.contestantStatsDao = contestantStatsDao;
    }

    @Override
    public ContestantStats getContestantStats(KingsUser kingsUser, Long contestantId) throws IOException {
        return contestantStatsDao.getById(contestantId).get();
    }
}
