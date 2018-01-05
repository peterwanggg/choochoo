package com.pwang.kings.tasks;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.pwang.kings.categories.CategoryTypeManagerFactory;
import com.pwang.kings.db.daos.ContestantRankDao;
import com.pwang.kings.db.daos.ContestantStatsDao;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.stats.ContestantRanker;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author pwang on 1/4/18.
 */
public final class ContestantRankerTask extends AbstractScheduledService {

    private final Logger LOGGER = Logger.getLogger(ContestantRankerTask.class);

    private final CategoryTypeManagerFactory categoryTypeManagerFactory;
    private final ContestantStatsDao contestantStatsDao;
    private final ContestantRankDao contestantRankDao;
    private final LocationDao locationDao;
    private final ContestantRanker ranker;

    private final int refreshPeriodInSeconds;

    public ContestantRankerTask(CategoryTypeManagerFactory categoryTypeManagerFactory,
                                ContestantStatsDao contestantStatsDao,
                                ContestantRankDao contestantRankDao,
                                LocationDao locationDao,
                                ContestantRanker ranker,
                                int refreshPeriodInSeconds) {

        this.categoryTypeManagerFactory = categoryTypeManagerFactory;
        this.contestantStatsDao = contestantStatsDao;
        this.contestantRankDao = contestantRankDao;
        this.locationDao = locationDao;
        this.ranker = ranker;
        this.refreshPeriodInSeconds = refreshPeriodInSeconds;
    }

    @Override
    protected void runOneIteration() throws Exception {
        LOGGER.info("starting contestant ranker task");
        categoryTypeManagerFactory.getAllCategoryManagers().forEach(categoryTypeManager ->
                locationDao.getAllLocations().forEach(location -> {
                            categoryTypeManager.getCategoriesByLocation(location.getLocationId()).values().forEach(category -> {
                                        LOGGER.debug("ranking category: " + category.getCategoryName());
                                        ranker.rankContestants(contestantStatsDao.getByCategoryId(category.getCategoryId()))
                                                .forEach(rank -> contestantRankDao.createRank(rank, ranker.getRankType().toString()));
                                    }
                            );
                        }
                )
        );
    }

    @Override
    protected Scheduler scheduler() {
        return AbstractScheduledService.Scheduler.newFixedRateSchedule(0, refreshPeriodInSeconds, TimeUnit.SECONDS);
    }
}
