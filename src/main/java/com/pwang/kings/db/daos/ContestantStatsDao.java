package com.pwang.kings.db.daos;

import com.pwang.kings.objects.stats.ContestantStats;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.util.List;
import java.util.Optional;

/**
 * @author pwang on 1/3/18.
 */
@UseStringTemplate3StatementLocator
public interface ContestantStatsDao {

    @SingleValueResult(ContestantStats.class)
    @SqlQuery(
            "SELECT * FROM stats.contestant WHERE contestant_id = :contestant_id"
    )
    Optional<ContestantStats> getById(@Bind("contestant_id") Long contestantId);

    @SqlQuery(
            "SELECT * FROM stats.contestant WHERE category_id = :category_id"
    )
    List<ContestantStats> getByCategoryId(@Bind("category_id") Long categoryId);

    @SqlQuery(
            "SELECT * FROM stats.contestant WHERE contestant_id IN (<contestant_ids>)"
    )
    List<ContestantStats> getByIds(@BindIn("contestant_ids") List<Long> contestantIds);
}
