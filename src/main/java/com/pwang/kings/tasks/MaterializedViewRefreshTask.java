package com.pwang.kings.tasks;

import com.google.common.util.concurrent.AbstractScheduledService;
import org.apache.log4j.Logger;
import org.skife.jdbi.v2.DBI;

import java.util.concurrent.TimeUnit;

/**
 * @author pwang on 1/3/18.
 */
public final class MaterializedViewRefreshTask extends AbstractScheduledService {

    private final Logger LOGGER = Logger.getLogger(MaterializedViewRefreshTask.class);
    private final DBI jdbi;
    private final int refreshPeriodInSeconds;
    private static final String REFRESH_STATEMENT = "REFRESH MATERIALIZED VIEW CONCURRENTLY stats.contestant";

    public MaterializedViewRefreshTask(DBI jdbi, int refreshPeriodInSeconds) {
        this.jdbi = jdbi;
        this.refreshPeriodInSeconds = refreshPeriodInSeconds;
    }

    @Override
    protected void runOneIteration() throws Exception {
        jdbi.withHandle(handle -> {
                    handle.execute(REFRESH_STATEMENT);
                    return null;
                }

        );
        LOGGER.info(REFRESH_STATEMENT);
    }

    @Override
    protected AbstractScheduledService.Scheduler scheduler() {
        return AbstractScheduledService.Scheduler.newFixedRateSchedule(0, refreshPeriodInSeconds, TimeUnit.SECONDS);
    }
}