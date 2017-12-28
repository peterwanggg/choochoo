package com.pwang.kings.db.daos;

import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.Location;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.Optional;

/**
 * @author pwang on 12/27/17.
 * <p>
 * CREATE TABLE IF NOT EXISTS common.location (
 * location_id BIGSERIAL PRIMARY KEY,
 * location_name text,
 * location_type text,
 * api_provider_type text,
 * api_provider_id text
 * );
 */
public interface LocationDao {

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.location "
                    + "(location_name, location_type, api_provider_type, api_provider_id) VALUES "
                    + "(:location.locationName, :location.locationType, :location.apiProviderType, :location.apiProviderId) "
                    + "ON CONFLICT ON CONSTRAINT location_api_id DO NOTHING"
    )
    Long create(@BindBean("location") Location location);

    @SingleValueResult(Location.class)
    @SqlQuery(
            "SELECT * FROM common.location WHERE api_provider_type = :api_provider_type AND api_provider_id = :api_provider_id"
    )
    Optional<Location> getByApiId(@Bind("api_provider_type") String apiProviderType, @Bind("api_provider_id") String apiProviderId);


}
