package com.pwang.kings.categories;

import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.KingsUser;
import com.pwang.kings.objects.model.Location;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author pwang on 12/26/17.
 */
public interface CategoryTypeManager {

    Optional<Location> getLocation(double lat, double lon) throws IOException;

    List<Location> getCitiesAndCreate(List<String> apiProviderIds) throws IOException;

    List<Contestant> getContestants(
            Location location,
            Long categoryId,
            Optional<Integer> page) throws IOException;

    List<Contestant> searchContestants(Location location, String contestantName) throws IOException;

    List<Contestant> getChallengers(
            KingsUser kingsUser,
            Location location,
            Category category,
            Contestant challenger,
            Optional<Integer> page) throws IOException;

    List<Contestant> getNewContestantsFromApi(Location location, Category category, int numNeeded) throws IOException;

    // <CategoryId, Category>
    Map<Long, Category> getCategoriesByLocation(Long locationId);

    List<Category> getTopCategoriesByLocation(Long locationId);

    List<Category> populateLocationCategories(Location location) throws IOException;


}
