package com.pwang.kings.resources;

import com.pwang.kings.api.ContestantService;
import com.pwang.kings.categories.CategoryManager;
import com.pwang.kings.categories.CategoryManagerFactory;
import com.pwang.kings.db.daos.CategoryDao;
import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.model.User;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


/**
 * @author pwang on 12/26/17.
 */
public final class ContestantResource implements ContestantService {

    Logger LOGGER = Logger.getLogger(ContestantResource.class);


    //    private final ZomatoService zomatoService;
    private final CategoryManagerFactory categoryManagerFactory;
    private final CategoryDao categoryDao;

    public ContestantResource(
//            ZomatoService zomatoService,
            CategoryDao categoryDao,
            CategoryManagerFactory categoryManagerFactory) {

//        this.zomatoService = zomatoService;
        this.categoryDao = categoryDao;
        this.categoryManagerFactory = categoryManagerFactory;
    }

//    @GET
//    public PlacesSearchResponse getContestants(@QueryParam("location") final LatLng latLng) throws InterruptedException, ApiException, IOException {
//
//        return PlacesApi.nearbySearchQuery(googleContext, latLng).radius(5000).await();
//    }

//
//    @GET
//    @Path("/search")
//    public SearchResult search(
//            @QueryParam("lat") double lat,
//            @QueryParam("lon") double lon) throws IOException {
//
//        Response<SearchResult> response = zomatoService.search(lat, lon).execute();
//        if (response.isSuccessful()) {
//            return response.body();
//        }
//
//        throw new WebApplicationException(response.message(), response.code());
//    }

    @Override
    public List<Contestant> getContestants(User user, double lat, double lon, long categoryId) {
//        Optional<Category> category = categoryDao.getById(categoryId);

        Category category = categoryDao.getById(categoryId)
                .orElseThrow(() ->
                        new WebApplicationException("could not find categoryId " + categoryId, HttpStatus.BAD_REQUEST_400));
        LOGGER.info("Got category: " + category);

        // 1. getCategoryManager(categoryId)
        CategoryManager categoryManager = categoryManagerFactory.getCategoryManager(category.getCategoryType());

        try {
            // 2. get location
            Optional<Location> location = categoryManager.getLocation(lat, lon);

            // 3.
            return categoryManager.getContestants(
                    user,
                    location.orElseThrow(() ->
                            new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501)),
                    categoryId);
        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }


    }
}
