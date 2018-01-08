package com.pwang.kings.adapters.zomato;


import com.pwang.kings.clients.ZomatoConstants;
import com.pwang.kings.objects.model.ApiProviderType;
import com.pwang.kings.objects.model.ImmutableLocation;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.model.LocationType;

/**
 * @author pwang on 1/6/18.
 */
public class LocationToLocationAdapter implements ZomatoAdapter<com.pwang.kings.objects.api.zomato.Location, Location> {


    @Override
    public Location adapt(com.pwang.kings.objects.api.zomato.Location apiObject) {
        LocationType locationType = LocationType.valueOf(apiObject.entityType());

        return ImmutableLocation.builder()
                .locationName(apiObject.name())
                .locationType(locationType.toString())
                .apiProviderType(ApiProviderType.zomato.toString())
                .apiProviderId(ZomatoConstants.toApiProviderId(locationType, apiObject.entityId()))
                .build();

    }
}
