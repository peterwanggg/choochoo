package com.pwang.kings.db.daos;

import com.pwang.kings.objects.action.Bout;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author pwang on 12/28/17.
 */
public interface BoutDao {

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.bout "
                    + "(category_id, winner_contestant_id, loser_contestant_id, kings_user_id) VALUES "
                    + "(:bout.categoryId, :bout.winnerContestantId, :bout.loserContestantId, :bout.kingsUserId) "
    )
    Long create(@BindBean("bout") Bout bout);

//    @SingleValueResult(Bout.class)
//    @SqlQuery(
//            "SELECT * FROM common.bout WHERE api_provider_type = :api_provider_type AND api_provider_id = :api_provider_id"
//    )
//    Optional<Bout> getByApiId(@Bind("api_provider_type") String apiProviderType, @Bind("api_provider_id") String apiProviderId);


}
