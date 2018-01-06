package com.pwang.kings.clients;

import com.pwang.kings.objects.model.LocationType;

/**
 * @author pwang on 1/2/18.
 */
public interface ZomatoConstants {
    enum Sort {
        cost,
        rating,
        real_distance
    }

    enum Order {
        desc,
        asc
    }

    static String toApiProviderId(LocationType locationType, Integer id) {
        return locationType.toString() + ":" + id;
    }

    static Integer locationApiProviderIdToId(String apiProviderId) {
        String[] parts = apiProviderId.split(":");
        if (parts.length == 1) {
            return Integer.valueOf(parts[0]);
        }
        return Integer.valueOf(parts[1]);

    }

}
