package com.pwang.kings.db.daos;

import com.pwang.kings.objects.action.Bout;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import java.util.List;
import java.util.Optional;

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

    @SingleValueResult(Long.class)
    @SqlQuery(
            "SELECT top_id\n" +
                    "FROM\n" +
                    "  (SELECT winner_contestant_id top_id,\n" +
                    "                               count(*)\n" +
                    "   FROM common.bout\n" +
                    "   WHERE kings_user_id = :kingsUserId\n" +
                    "     AND category_id = :categoryId\n" +
                    "     AND winner_contestant_id != :nextContestantId\n" +
                    "   GROUP BY 1 HAVING count(*) > 0\n" +
                    "   ORDER BY 2 DESC) AS top\n" +
                    "WHERE NOT EXISTS\n" +
                    "    (SELECT *\n" +
                    "     FROM common.bout\n" +
                    "     WHERE category_id = :categoryId\n" +
                    "       AND kings_user_id = :kingsUserId\n" +
                    "       AND ((winner_contestant_id = :nextContestantId\n" +
                    "             AND loser_contestant_id = top.top_id)\n" +
                    "            OR (loser_contestant_id = :nextContestantId\n" +
                    "                AND winner_contestant_id = top.top_id))) LIMIT 1"
    )
    Optional<Long> getBestContestantFromBout(
            @Bind("kingsUserId") Long userId,
            @Bind("nextContestantId") Long nextContestantId,
            @Bind("categoryId") Long nextCategoryId);

    @SingleValueResult(Long.class)
    @SqlQuery(
            "SELECT contestant_id\n" +
                    "FROM\n" +
                    "  (SELECT contestant_id\n" +
                    "   FROM common.contestant\n" +
                    "   WHERE category_id = :categoryId\n" +
                    "     AND contestant_id != :nextContestantId\n" +
                    "   ORDER BY contestant_id ASC) AS top\n" +
                    "WHERE NOT EXISTS\n" +
                    "    (SELECT *\n" +
                    "     FROM common.bout\n" +
                    "     WHERE category_id = :categoryId\n" +
                    "       AND kings_user_id = :kingsUserId\n" +
                    "       AND ((winner_contestant_id = :nextContestantId\n" +
                    "             AND loser_contestant_id = top.contestant_id)\n" +
                    "            OR (loser_contestant_id = :nextContestantId\n" +
                    "                AND winner_contestant_id = top.contestant_id))) LIMIT 1;"
    )
    Optional<Long> getBestContestantFromContestant(
            @Bind("kingsUserId") Long userId,
            @Bind("nextContestantId") Long nextContestantId,
            @Bind("categoryId") Long nextCategoryId);


}
