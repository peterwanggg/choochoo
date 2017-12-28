package com.pwang.kings.categories;

import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.KingsUser;
import com.pwang.kings.objects.model.Location;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author pwang on 12/26/17.
 */
public interface CategoryManager {

    Optional<Location> getLocation(double lat, double lon) throws IOException;

    List<Location> getLocations(List<String> apiProviderIds) throws IOException;

    List<Contestant> getContestants(KingsUser kingsUser, Location location, Category category) throws IOException;

    List<Category> populateCategory(Location location) throws IOException;

}
