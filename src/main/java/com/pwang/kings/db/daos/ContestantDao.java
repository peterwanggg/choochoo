package com.pwang.kings.db.daos;

import com.pwang.kings.objects.model.Contestant;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.Optional;

/**
 * @author pwang on 12/27/17.
 */
public interface ContestantDao {

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.contestant "
                    + "(category_id, contestant_name, image_url, api_provider_type, api_provider_id) VALUES "
                    + "(:contestant.categoryId, :contestant.contestantName, :imageUrl, :contestant.apiProviderType, :contestant.apiProviderId) "
                    + "ON CONFLICT ON CONSTRAINT contestant_api_id DO NOTHING"
    )
    Long create(@BindBean("contestant") Contestant contestant, @Bind("imageUrl") String imageUrl);

    @SingleValueResult(Contestant.class)
    @SqlQuery(
            "SELECT * FROM common.contestant WHERE api_provider_type = :api_provider_type AND api_provider_id = :api_provider_id"
    )
    Optional<Contestant> getByApiId(@Bind("api_provider_type") String apiProviderType, @Bind("api_provider_id") String apiProviderId);

}
