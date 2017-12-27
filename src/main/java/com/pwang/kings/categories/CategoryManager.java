package com.pwang.kings.categories;

import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.model.User;

import java.util.List;

/**
 * @author pwang on 12/26/17.
 */
public interface CategoryManager {

    Location getLocation(double lat, double lon);

    List<Contestant> getContestants(User user, Location location, int categoryId);



}
