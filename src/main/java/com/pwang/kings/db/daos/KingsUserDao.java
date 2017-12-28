package com.pwang.kings.db.daos;

import com.pwang.kings.objects.model.KingsUser;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.Optional;

/**
 * @author pwang on 12/28/17.
 */
public interface KingsUserDao {

    @SingleValueResult(KingsUser.class)
    @SqlQuery(
            "SELECT * FROM common.kings_user WHERE name = :name"
    )
    Optional<KingsUser> getByName(@Bind("name") String name);


}
