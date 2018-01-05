package com.pwang.kings.categories;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.pwang.kings.KingsConstants;
import com.pwang.kings.adapters.zomato.CityToLocationAdapter;
import com.pwang.kings.adapters.zomato.CuisineToCategoryAdapter;
import com.pwang.kings.adapters.zomato.RestaurantToContestantAdapter;
import com.pwang.kings.clients.ZomatoConstants;
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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author pwang on 12/26/17.
 */
public final class RestaurantCategoryManager implements CategoryTypeManager {

    private static Logger LOGGER = Logger.getLogger(RestaurantCategoryManager.class);
    private static final String DEFAULT_SORT = ZomatoConstants.Sort.rating.toString();
    private static final String DEFAULT_ORDER = ZomatoConstants.Order.desc.toString();

    private final CityToLocationAdapter cityToLocationAdapter = new CityToLocationAdapter();
    private final RestaurantToContestantAdapter restaurantToContestantAdapter = new RestaurantToContestantAdapter();
    private final CuisineToCategoryAdapter cuisineToCategoryAdapter = new CuisineToCategoryAdapter();

    private final ZomatoService zomatoService;
    private final LocationDao locationDao;
    private final CategoryDao categoryDao;
    private final ContestantDao contestantDao;

    private final Optional<Integer> cityIdOverride;

    // <LocationId, <CategoryName, Category>
    private final LoadingCache<Long, Map<Long, Category>> categoryCache;
    private final LoadingCache<Long, List<Long>> topCategoryIds;
    private final LoadingCache<Long, Boolean> exhaustedCategories;

    RestaurantCategoryManager(
            ZomatoService zomatoService,
            LocationDao locationDao,
            CategoryDao categoryDao,
            ContestantDao contestantDao,
            Optional<Integer> cityIdOverride) {
        this.zomatoService = zomatoService;
        this.locationDao = locationDao;
        this.categoryDao = categoryDao;
        this.contestantDao = contestantDao;

        this.cityIdOverride = cityIdOverride;

        this.categoryCache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<Long, Map<Long, Category>>() {
                    @Override
                    public Map<Long, Category> load(Long key) throws Exception {
                        return categoryDao.getByLocationCategoryType(key, CategoryType.restaurant.toString())
                                .stream().collect(Collectors.toMap(
                                        Category::getCategoryId,
                                        Function.identity(),
                                        (v1, v2) -> {
                                            throw new WebApplicationException(String.format("Duplicate key for values %s and %s", v1, v2), HttpStatus.INTERNAL_SERVER_ERROR_500);
                                        },
                                        TreeMap::new
                                ));
                    }
                });

        this.topCategoryIds = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<Long, List<Long>>() {
                    @Override
                    public List<Long> load(Long key) {
                        return categoryDao.getTopByBoutCountLocationCategoryType(
                                key, CategoryType.restaurant.toString());
                    }
                });

        this.exhaustedCategories = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<Long, Boolean>() {
                    @Override
                    public Boolean load(Long key) {
                        return false;
                    }
                });
    }

    @Override
    public Optional<Location> getLocation(double lat, double lon) throws IOException {
        if (cityIdOverride.isPresent()) {
            return locationDao.getById(cityIdOverride.get());
        }

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
    public List<Contestant> searchContestants(Location location, String contestantName) throws IOException {
        Response<SearchResult> response = zomatoService.search(
                Integer.valueOf(location.getApiProviderId()),
                EntityType.city.toString(),
                contestantName,
                null,
                null,
                null,
                null,
                DEFAULT_SORT,
                DEFAULT_ORDER).execute();
        if (!response.isSuccessful()) {
            throw new WebApplicationException("zomato request failed", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        Map<String, Category> categoryMap = categoryCache.getUnchecked(location.getLocationId())
                .values().stream().collect(Collectors.toMap(Category::getCategoryName, Function.identity()));

        return response.body()
                .getRestaurants().stream()
                // filter out cuisines we don't have, likely due to null 'cuisines' on the RestaurantValue
                .map(restaurant -> {
                    String cuisine = RestaurantToContestantAdapter.getCuisine(restaurant.getRestaurant().getCuisines());
                    Category category = categoryMap.get(cuisine);
                    if (category == null) {
                        return null;
                    }
                    Contestant contestant = restaurantToContestantAdapter.adapt(restaurant);

                    // check if it's in db already
                    Optional<Contestant> contestantMaybe = contestantDao.getByApiId(ApiProviderType.zomato.toString(), String.valueOf(contestant.getApiProviderId()));
                    if (contestantMaybe.isPresent()) {
                        return contestantMaybe.get();
                    }

                    // create if not
                    Contestant creatableContestant = ImmutableContestant.builder()
                            .from(contestant)
                            .categoryId(category.getCategoryId())
                            .build();
                    return ImmutableContestant.builder()
                            .from(creatableContestant)
                            .contestantId(contestantDao.create(creatableContestant, creatableContestant.getImageUrl().toString()))
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Contestant> getContestants(
            KingsUser kingsUser,
            Location location,
            Category category,
            Optional<Integer> offset) throws IOException {
        // 1. get from DB
        List<Contestant> contestants = contestantDao.getNewContestantsForUser(
                kingsUser.getKingsUserId(),
                category.getCategoryId(),
                KingsConstants.CONTESTANTS_PAGE_MIN_SIZE,
                offset.orElse(0));

        if (contestants.size() < KingsConstants.CONTESTANTS_PAGE_MIN_SIZE) {
            contestants.addAll(getNewContestantFromZomato(location, category, KingsConstants.CONTESTANTS_PAGE_MIN_SIZE - contestants.size()));
        }
        return contestants;
    }

    @Override
    public List<Contestant> getChallengers(
            KingsUser kingsUser,
            Location location,
            Category category,
            Contestant challenger,
            Optional<Integer> offset) throws IOException {

        // 1. get from DB
        List<Contestant> contestants = contestantDao.getNewChallengersForUser(
                kingsUser.getKingsUserId(),
                category.getCategoryId(),
                challenger.getContestantId(),
                KingsConstants.CONTESTANTS_PAGE_MIN_SIZE,
                offset.orElse(0));

        if (contestants.size() < KingsConstants.CONTESTANTS_PAGE_MIN_SIZE) {
            contestants.addAll(getNewContestantFromZomato(location, category, KingsConstants.CONTESTANTS_PAGE_MIN_SIZE - contestants.size()));
        }
        return contestants;
    }

    private List<Contestant> getNewContestantFromZomato(Location location, Category category, int numNeeded) throws IOException {
        int page = 0;
        List<Contestant> insertedContestants = new ArrayList<>();
        if (exhaustedCategories.getUnchecked(category.getCategoryId())) {
            LOGGER.debug("category " + category.getCategoryName() + " is already exhausted");
            return insertedContestants;
        }

        while (insertedContestants.size() < numNeeded) {
            Response<SearchResult> response = zomatoService.search(
                    Integer.valueOf(location.getApiProviderId()),
                    EntityType.city.toString(),
                    null,
                    category.getApiProviderId(),
                    null,
                    null,
                    page * ZomatoService.SEARCH_PAGE_SIZE,
                    DEFAULT_SORT,
                    DEFAULT_ORDER).execute();
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
        if (insertedContestants.isEmpty()) {
            LOGGER.info("just exhausted category " + category.getCategoryName());
            exhaustedCategories.put(category.getCategoryId(), true);
        }
        LOGGER.info("fetched " + insertedContestants.size() + " new contestants after " + page + " pages");
        return insertedContestants;
    }

    @Override
    public Map<Long, Category> getCategoriesByLocation(Long locationId) {
        return categoryCache.getUnchecked(locationId);
    }

    @Override
    public List<Category> getTopCategoriesByLocation(Long locationId) {
        Map<Long, Category> categories = categoryCache.getUnchecked(locationId);

        List<Long> topCatIds = topCategoryIds.getUnchecked(locationId);

        return topCategoryIds.getUnchecked(locationId).stream()
                .map(categoryId -> categories.get(categoryId))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> populateLocationCategories(Location location) throws IOException {
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
