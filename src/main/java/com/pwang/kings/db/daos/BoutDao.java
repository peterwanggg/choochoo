package com.pwang.kings.db.daos;

import com.pwang.kings.db.util.SqlLogger;
import com.pwang.kings.objects.action.Bout;
import com.pwang.kings.objects.db.LongPair;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.Optional;

/**
 * @author pwang on 12/:contestant_id/17.
 */
@SqlLogger
@UseStringTemplate3StatementLocator
public interface BoutDao {

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.bout "
                    + "(category_id, winner_contestant_id, loser_contestant_id, kings_user_id) VALUES "
                    + "(:bout.categoryId, :bout.winnerContestantId, :bout.loserContestantId, :bout.kingsUserId) "
    )
    Long create(@BindBean("bout") Bout bout);

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
                    "                AND winner_contestant_id = top.top_id))) AND\n" +
                    "    top.top_id NOT IN\n" +
                    "       (SELECT unnest(contestant_ids) FROM common.contestant_skips WHERE\n" +
                    "           kings_user_id = :kingsUserId and category_id = :categoryId)\n" +
                    "LIMIT 1"
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
                    "                AND winner_contestant_id = top.contestant_id))) AND\n" +
                    "    contestant_id NOT IN\n" +
                    "       (SELECT unnest(contestant_ids) FROM common.contestant_skips WHERE \n" +
                    "           kings_user_id = :kingsUserId and category_id = :categoryId)\n" +
                    "LIMIT 1"
    )
    Optional<Long> getBestContestantFromContestant(
            @Bind("kingsUserId") Long userId,
            @Bind("nextContestantId") Long nextContestantId,
            @Bind("categoryId") Long nextCategoryId);

    @SingleValueResult(LongPair.class)
    @SqlQuery(
            "SELECT a_id as left, b_id as right\n" +
                    "FROM\n" +
                    "  (SELECT winner_contestant_id AS a_id,\n" +
                    "          count(*) AS a_wins\n" +
                    "   FROM common.bout\n" +
                    "   WHERE kings_user_id = :kingsUserId\n" +
                    "     AND category_id = :categoryId\n" +
                    "   GROUP BY 1\n" +
                    "   ORDER BY 2 DESC) AS a,\n" +
                    "\n" +
                    "  (SELECT winner_contestant_id AS b_id,\n" +
                    "          count(*) AS b_wins\n" +
                    "   FROM common.bout\n" +
                    "   WHERE kings_user_id = :kingsUserId\n" +
                    "     AND category_id = :categoryId\n" +
                    "   GROUP BY 1\n" +
                    "   ORDER BY 2 DESC) AS b\n" +
                    "WHERE a_id != b_id\n" +
                    "  AND NOT EXISTS\n" +
                    "    (SELECT *\n" +
                    "     FROM common.bout\n" +
                    "     WHERE kings_user_id = :kingsUserId\n" +
                    "       AND category_id = :categoryId\n" +
                    "       AND ((winner_contestant_id = a_id\n" +
                    "             AND loser_contestant_id = b_id)\n" +
                    "            OR (loser_contestant_id = a_id\n" +
                    "                AND winner_contestant_id = b_id))) AND\n" +
                    "    a_id NOT IN " +
                    "       (SELECT unnest(contestant_ids) FROM common.contestant_skips WHERE \n" +
                    "           kings_user_id = :kingsUserId and category_id = :categoryId) AND\n" +
                    "    b_id NOT IN \n" +
                    "       (SELECT unnest(contestant_ids) FROM common.contestant_skips WHERE\n" +
                    "           kings_user_id = :kingsUserId and category_id = :categoryId)\n" +
                    "LIMIT 1"
    )
    Optional<LongPair> getBestNewMatch(
            @Bind("kingsUserId") Long userId,
            @Bind("categoryId") Long nextCategoryId);


}
