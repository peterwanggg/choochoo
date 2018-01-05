package com.pwang.kings.db.daos;

import com.pwang.kings.objects.stats.ContestantRank;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.util.List;

/**
 * @author pwang on 1/4/18.
 */
@UseStringTemplate3StatementLocator
public interface ContestantRankDao {

    @SqlUpdate(
            "INSERT INTO stats.contestant_rank (contestant_id, category_id, rank, rank_type) " +
                    " VALUES (:contestant_rank.contestantId, :contestant_rank.categoryId,q :contestant_rank.rank, :rank_type) "
                    + " ON CONFLICT (contestant_id) DO UPDATE SET rank = EXCLUDED.rank"
    )
    void createRank(@BindBean("contestant_rank") ContestantRank rank, @Bind("rank_type") String rankType);

    @SqlQuery(
            "SELECT * FROM stats.contestant_rank WHERE contestant_id IN (<contestant_ids>)"
    )
    List<ContestantRank> getByIds(@BindIn("contestant_ids") List<Long> contestantIds);

}
