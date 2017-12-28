package com.pwang.kings.categories;

import com.pwang.kings.adapters.zomato.CityToLocationAdapter;
import com.pwang.kings.adapters.zomato.CuisineToCategoryAdapter;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.db.daos.CategoryDao;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.objects.api.zomato.CitiesResult;
import com.pwang.kings.objects.api.zomato.CuisinesResult;
import com.pwang.kings.objects.model.*;
import org.eclipse.jetty.http.HttpStatus;
import retrofit2.Response;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author pwang on 12/26/17.
 */
public final class RestaurantCategoryManager implements CategoryManager {

    private final CityToLocationAdapter cityToLocationAdapter = new CityToLocationAdapter();
    private final CuisineToCategoryAdapter cuisineToCategoryAdapter = new CuisineToCategoryAdapter();

    private final ZomatoService zomatoService;
    private final LocationDao locationDao;
    private final CategoryDao categoryDao;

    RestaurantCategoryManager(
            ZomatoService zomatoService,
            LocationDao locationDao, CategoryDao categoryDao) {
        this.zomatoService = zomatoService;
        this.locationDao = locationDao;
        this.categoryDao = categoryDao;
    }

    @Override
    public Optional<Location> getLocation(double lat, double lon) throws IOException {
        Response<CitiesResult> response = zomatoService.cities(lat, lon, null, 1).execute();
        if (!response.isSuccessful()) {
            throw new WebApplicationException("zomato request failed", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
        return response.body()
                .locationSuggestions().stream().findFirst()
                .map(cityToLocationAdapter::adapt)
                .map(location ->
                        ImmutableLocation.builder()
                                .from(location)
                                .locationId(locationDao.create(location))
                                .build());
    }

    @Override
    public List<Location> getLocations(List<String> apiProviderIds) throws IOException {
        Response<CitiesResult> response = zomatoService.cities(
                null,
                null,
                apiProviderIds.stream().map(Integer::new)
                        .map(Object::toString)
                        .collect(Collectors.joining(",")),
                null).execute();
        if (!response.isSuccessful()) {
            throw new WebApplicationException("zomato request failed", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        return response.body()
                .locationSuggestions().stream()
                .map(cityToLocationAdapter::adapt)
                .map(location ->
                        ImmutableLocation.builder()
                                .from(location)
                                .locationId(locationDao.create(location))
                                .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Contestant> getContestants(User user, Location location, long categoryId) {
        return null;
    }

    @Override
    public List<Category> populateCategory(Location location) throws IOException {
        Response<CuisinesResult> response = zomatoService
                .cuisines(Integer.valueOf(location.getApiProviderId()))
                .execute();
        if (!response.isSuccessful()) {
            throw new WebApplicationException("zomato request failed", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        return response.body()
                .cuisines().stream()
                .map(cuisineToCategoryAdapter::adapt)
                .map(category ->
                        ImmutableCategory.builder()
                                .from(category)
                                .categoryId(categoryDao.create(category))
                                .build())
                .collect(Collectors.toList());
    }

}
