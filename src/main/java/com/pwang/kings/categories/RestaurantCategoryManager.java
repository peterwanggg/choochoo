package com.pwang.kings.categories;

import com.pwang.kings.adapters.zomato.CityToLocationAdapter;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.objects.api.zomato.CitiesResult;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.ImmutableLocation;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.model.User;
import org.eclipse.jetty.http.HttpStatus;
import retrofit2.Response;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author pwang on 12/26/17.
 */
public final class RestaurantCategoryManager implements CategoryManager {

    private final ZomatoService zomatoService;
    private final LocationDao locationDao;
    private final CityToLocationAdapter cityToLocationAdapter = new CityToLocationAdapter();

    RestaurantCategoryManager(
            ZomatoService zomatoService,
            LocationDao locationDao) {
        this.zomatoService = zomatoService;
        this.locationDao = locationDao;
    }

    @Override
    public Optional<Location> getLocation(double lat, double lon) throws IOException {
        Response<CitiesResult> response = zomatoService.cities(lat, lon, 1).execute();
        if (!response.isSuccessful()) {
            throw new WebApplicationException("zomato request failed", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        return response.body()
                .locationSuggestions().stream().findFirst()
                .map(cityToLocationAdapter::adapt).map(location ->
                {
                    System.out.println(location);
                    return

                            ImmutableLocation.builder()
                                    .from(location)
                                    .locationId(locationDao.create(location))
                                    .build();
                });


    }

    @Override
    public List<Contestant> getContestants(User user, Location location, long categoryId) {
        return null;
    }

}
