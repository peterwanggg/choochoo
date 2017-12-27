package com.pwang.kings.categories;

import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.model.User;

import java.util.List;

/**
 * @author pwang on 12/26/17.
 */
public class RestaurantCategoryManager implements CategoryManager {
    @Override
    public Location getLocation(double lat, double lon) {
        return null;
    }

    @Override
    public List<Contestant> getContestants(User user, Location location, int categoryId) {
        return null;
    }
}
