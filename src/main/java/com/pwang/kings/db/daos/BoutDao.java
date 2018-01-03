package com.pwang.kings.db.daos;

import com.pwang.kings.objects.action.Bout;
import org.skife.jdbi.v2.sqlobject.*;

import java.util.List;

/**
 * @author pwang on 12/:contestant_id/17.
 */
public interface BoutDao {

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.bout "
                    + "(category_id, winner_contestant_id, loser_contestant_id, kings_user_id) VALUES "
                    + "(:bout.categoryId, :bout.winnerContestantId, :bout.loserContestantId, :bout.kingsUserId) "
    )
    Long create(@BindBean("bout") Bout bout);

    @SqlQuery(
            "SELECT * FROM common.bout WHERE winner_contestant_id = :contestant_id or loser_contestant_id = :contestant_id;"
    )
    List<Bout> getContestantBouts(@Bind("contestant_id") Long contestantId);


}
