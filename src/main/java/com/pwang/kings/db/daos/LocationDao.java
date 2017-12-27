package com.pwang.kings.db.daos;

import com.pwang.kings.objects.model.Location;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

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
//                    "(:location.location_name, :location.location_type, :location.api_provider_type, :location.api_provider_id) " +
                    + "(:location.locationName, :location.locationType, :location.apiProviderType, :location.apiProviderId) "
                    + "ON CONFLICT ON CONSTRAINT api_id DO NOTHING"
    )
    Long create(@BindBean("location") Location location);
}
