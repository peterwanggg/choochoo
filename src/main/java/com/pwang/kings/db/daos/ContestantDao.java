package com.pwang.kings.db.daos;

import com.pwang.kings.objects.model.Contestant;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author pwang on 12/27/17.
 */
public interface ContestantDao {

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.contestant "
                    + "(category_id, contestant_name, image_url, api_provider_type, api_provider_id) VALUES "
                    + "(:contesant.categoryId, :contestant.contestantName, :contestant.imageUrl, :contestant.apiProviderType, :contestant.apiProviderId) "
                    + "ON CONFLICT ON CONSTRAINT contesant_api_id DO NOTHING"
    )
    Long create(@BindBean("contestant") Contestant contestant);

//    @SingleValueResult(Con.class)
//    @SqlQuery(
//            "SELECT * FROM common.contestant WHERE api_provider_type = :api_provider_type AND api_provider_id = :api_provider_id"
//    )
//    Optional<Location> getByApiId(@Bind("api_provider_type") String apiProviderType, @Bind("api_provider_id") String apiProviderId);

}
