package com.pwang.kings.categories;

import com.pwang.kings.adapters.zomato.CityToLocationAdapter;
import com.pwang.kings.adapters.zomato.CuisineToCategoryAdapter;
import com.pwang.kings.adapters.zomato.RestaurantToContestantAdapter;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.db.daos.CategoryDao;
import com.pwang.kings.db.daos.ContestantDao;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.objects.api.zomato.CitiesResult;
import com.pwang.kings.objects.api.zomato.CuisinesResult;
import com.pwang.kings.objects.api.zomato.EntityType;
import com.pwang.kings.objects.api.zomato.SearchResult;
import com.pwang.kings.objects.model.*;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import retrofit2.Response;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author pwang on 12/26/17.
 */
public final class RestaurantCategoryManager implements CategoryManager {

    private static Logger LOGGER = Logger.getLogger(RestaurantCategoryManager.class);
    private static final int CONTESTANTS_MIN_SIZE = 10;

    private final CityToLocationAdapter cityToLocationAdapter = new CityToLocationAdapter();
    private final RestaurantToContestantAdapter restaurantToContestantAdapter = new RestaurantToContestantAdapter();
    private final CuisineToCategoryAdapter cuisineToCategoryAdapter = new CuisineToCategoryAdapter();

    private final ZomatoService zomatoService;
    private final LocationDao locationDao;
    private final CategoryDao categoryDao;
    private final ContestantDao contestantDao;

    RestaurantCategoryManager(
            ZomatoService zomatoService,
            LocationDao locationDao, CategoryDao categoryDao, ContestantDao contestantDao) {
        this.zomatoService = zomatoService;
        this.locationDao = locationDao;
        this.categoryDao = categoryDao;
        this.contestantDao = contestantDao;
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
                .map(location -> {
                            Optional<Location> dbLocation = locationDao.getByApiId(location.getApiProviderType(), location.getApiProviderId());
                            Long locationId;
                            if (dbLocation.isPresent()) {
                                locationId = dbLocation.get().getLocationId();
                            } else {
                                locationId = locationDao.create(location);
                            }
                            return ImmutableLocation.builder()
                                    .from(location)
                                    .locationId(locationId)
                                    .build();
                        }
                );
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
    public List<Contestant> getContestants(KingsUser kingsUser, Location location, Category category) throws IOException {
        // 1. get from DB
        List<Contestant> contestants = contestantDao.getNewContestantsForUser(
                kingsUser.getKingsUserId(),
                category.getCategoryId(),
                CONTESTANTS_MIN_SIZE);

        if (contestants.size() < CONTESTANTS_MIN_SIZE) {
            contestants.addAll(getContestantFromZomato(location, category, CONTESTANTS_MIN_SIZE - contestants.size()));
        }
        return contestants;
    }


    @Override
    public List<Contestant> getChallengers(KingsUser kingsUser, Location location, Category category, Contestant challenger) throws IOException {
        // 1. get from DB
        List<Contestant> contestants = contestantDao.getNewChallengersForUser(
                kingsUser.getKingsUserId(),
                category.getCategoryId(),
                challenger.getContestantId(),
                CONTESTANTS_MIN_SIZE);

        if (contestants.size() < CONTESTANTS_MIN_SIZE) {
            contestants.addAll(getContestantFromZomato(location, category, CONTESTANTS_MIN_SIZE - contestants.size()));
        }
        return contestants;
    }

    private List<Contestant> getContestantFromZomato(Location location, Category category, int numNeeded) throws IOException {
        int page = 0;
        List<Contestant> insertedContestants = new ArrayList<>();

        while (insertedContestants.size() < numNeeded) {
            Response<SearchResult> response = zomatoService.search(
                    Integer.valueOf(location.getApiProviderId()),
                    EntityType.city.toString(),
                    category.getApiProviderId(),
                    null,
                    null,
                    page * ZomatoService.SEARCH_PAGE_SIZE).execute();
            if (!response.isSuccessful()) {
                throw new WebApplicationException("zomato request failed", HttpStatus.INTERNAL_SERVER_ERROR_500);
            }
            if (response.body().getRestaurants().size() == 0) {
                LOGGER.info("retrieved all results after page " + page);
                break;
            }
            insertedContestants.addAll(
                    response.body()
                            .getRestaurants().stream()
                            // only new restaurants
                            .filter(restaurant ->
                                    !contestantDao.getByApiId(ApiProviderType.zomato.toString(), String.valueOf(restaurant.getRestaurant().getId()))
                                            .isPresent())
                            .map(restaurantToContestantAdapter::adapt)
                            .map(contestant ->
                                    ImmutableContestant.builder()
                                            .from(contestant)
                                            .categoryId(category.getCategoryId())
                                            .build())
                            .map(contestant ->
                                    ImmutableContestant.builder()
                                            .from(contestant)
                                            .contestantId(contestantDao.create(contestant, contestant.getImageUrl().toString()))
                                            .build())
                            .collect(Collectors.toList()));
            page++;
        }
        LOGGER.info("fetched " + insertedContestants.size() + " new contestants after " + page + " pages");
        return insertedContestants;
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
                                .locationId(location.getLocationId())
                                .build())
                .map(category ->
                        ImmutableCategory.builder()
                                .from(category)
                                .categoryId(categoryDao.create(category))
                                .build())
                .collect(Collectors.toList());
    }


}
