package com.pwang.kings.db.daos;

import com.pwang.kings.objects.model.Location;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.List;
import java.util.Optional;

/**
 * @author pwang on 12/27/17.
 */
public interface LocationDao {

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.location "
                    + "(location_name, location_type, api_provider_type, api_provider_id) VALUES "
                    + "(:location.locationName, :location.locationType, :location.apiProviderType, :location.apiProviderId) "
                    + "ON CONFLICT ON CONSTRAINT location_api_key DO NOTHING"
    )
    Long create(@BindBean("location") Location location);

    @GetGeneratedKeys
    @SqlUpdate(
            "INSERT INTO common.location "
                    + "(location_name, location_type, parent_location_id, api_provider_type, api_provider_id) VALUES "
                    + "(:location.locationName, :location.locationType, :parent_location_id, :location.apiProviderType, :location.apiProviderId) "
                    + "ON CONFLICT ON CONSTRAINT location_api_key DO NOTHING"
    )
    Long create(@BindBean("location") Location location, @Bind("parent_location_id") Long parentLocationId);

    @SingleValueResult(Location.class)
    @SqlQuery(
            "SELECT * FROM common.location WHERE location_id = :location_id"
    )
    Optional<Location> getById(@Bind("location_id") Integer locationId);

    @SingleValueResult(Location.class)
    @SqlQuery(
            "SELECT * FROM common.location WHERE api_provider_type = :api_provider_type AND api_provider_id = :api_provider_id"
    )
    Optional<Location> getByApiId(@Bind("api_provider_type") String apiProviderType, @Bind("api_provider_id") String apiProviderId);


    @SqlQuery(
            "SELECT * FROM common.location"
    )
    List<Location> getAllLocations();
}
