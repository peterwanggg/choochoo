package com.pwang.kings.db.daos;

import com.pwang.kings.db.util.SqlLogger;
import com.pwang.kings.objects.model.ContestantSkips;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.Optional;

/**
 * @author pwang on 1/18/18.
 */
@SqlLogger
public interface ContestantSkipsDao {

    @SingleValueResult(ContestantSkips.class)
    @SqlQuery(
            "SELECT * from common.contestant_skips WHERE kings_user_id = :kingsUserId"
    )
    Optional<ContestantSkips> getById(@Bind("kingsUserId") Long kingsUserId);

    @SqlUpdate(
            "INSERT INTO common.contestant_skips AS s\n" +
                    "VALUES (:kingsUserId,\n" +
                    "        :categoryId,\n" +
                    "        array[ :contestantId ] ) ON conflict ON CONSTRAINT skips_user_category DO\n" +
                    "UPDATE\n" +
                    "SET contestant_ids = array_cat(s.contestant_ids, array[ :contestantId ]::bigint[] );"
    )
    void addById(@Bind("kingsUserId") Long kingsUserId,
                 @Bind("categoryId") Long categoryId,
                 @Bind("contestantId") Long contestantId);

    @SqlUpdate(
            "UPDATE common.contestant_skips AS s\n" +
                    "SET contestant_ids = array_remove(s.contestant_ids, :contestantId::bigint)\n" +
                    "WHERE s.category_id = :categoryId\n" +
                    "  AND s.kings_user_id = :kingsUserId"
    )
    void removeById(@Bind("kingsUserId") Long kingsUserId,
                    @Bind("categoryId") Long categoryId,
                    @Bind("contestantId") Long contestantId);
}
