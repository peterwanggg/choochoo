package com.pwang.kings.db.daos;

import com.pwang.kings.objects.action.Bout;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.util.List;

/**
 * @author pwang on 12/:contestant_id/17.
 */
@UseStringTemplate3StatementLocator
public interface BoutDao {

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.bout "
                    + "(category_id, winner_contestant_id, loser_contestant_id, kings_user_id) VALUES "
                    + "(:bout.categoryId, :bout.winnerContestantId, :bout.loserContestantId, :bout.kingsUserId) "
    )
    Long create(@BindBean("bout") Bout bout);

    @SqlQuery(
            "SELECT * FROM common.bout WHERE kings_user_id = :user_id " +
                    "AND ( winner_contestant_id IN (<contestant_ids>) OR loser_contestant_id IN (<contestant_ids>) )"
    )
    List<Bout> getBoutsByUserAndContestantIds(@Bind("user_id") Long userId, @BindIn("contestant_ids") List<Long> contestantIds);


}
