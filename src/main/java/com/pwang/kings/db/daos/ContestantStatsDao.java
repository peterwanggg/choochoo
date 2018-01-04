package com.pwang.kings.db.daos;

import com.pwang.kings.objects.stats.ContestantStats;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.Optional;

/**
 * @author pwang on 1/3/18.
 */
public interface ContestantStatsDao {

    @SingleValueResult(ContestantStats.class)
    @SqlQuery(
            "SELECT * FROM stats.contestant WHERE contestant_id = :contestant_id"
    )
    Optional<ContestantStats> getById(@Bind("contestant_id") Long contestantId);
}
