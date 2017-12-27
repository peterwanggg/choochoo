package com.pwang.kings.resources;

import com.pwang.kings.api.ContestantService;
import com.pwang.kings.categories.CategoryManager;
import com.pwang.kings.categories.CategoryManagerFactory;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.db.daos.CategoryDao;
import com.pwang.kings.objects.api.zomato.SearchResult;
import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.model.User;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import retrofit2.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


/**
 * @author pwang on 12/26/17.
 */
public final class ContestantResource implements ContestantService {

    Logger LOGGER = Logger.getLogger(ContestantResource.class);


    private final ZomatoService zomatoService;
    private final CategoryManagerFactory categoryManagerFactory;
    private final CategoryDao categoryDao;

    public ContestantResource(
            ZomatoService zomatoService,
            CategoryDao categoryDao,
            CategoryManagerFactory categoryManagerFactory) {

        this.zomatoService = zomatoService;
        this.categoryDao = categoryDao;
        this.categoryManagerFactory = categoryManagerFactory;
    }

//    @GET
//    public PlacesSearchResponse getContestants(@QueryParam("location") final LatLng latLng) throws InterruptedException, ApiException, IOException {
//
//        return PlacesApi.nearbySearchQuery(googleContext, latLng).radius(5000).await();
//    }


    @GET
    @Path("/search")
    public SearchResult search(
            @QueryParam("lat") double lat,
            @QueryParam("lon") double lon) throws IOException {

        Response<SearchResult> response = zomatoService.search(lat, lon).execute();
        if (response.isSuccessful()) {
            return response.body();
        }

        throw new WebApplicationException(response.message(), response.code());
    }

    @Override
    public List<Contestant> getContestants(User user, double lat, double lon, int categoryId) {
//        Optional<Category> category = categoryDao.getById(categoryId);

        Category category = categoryDao.getById(categoryId).orElseThrow(() -> new WebApplicationException("could not find categoryId " + categoryId, HttpStatus.BAD_REQUEST_400));
        LOGGER.info("Got category: " + category);
        return null;


//
//        // 1. getCategoryManager(categoryId)
//        CategoryManager categoryManager = categoryManagerFactory.getCategoryManager(null);
//
//        // 2. getLocation
//        Location location = categoryManager.getLocation(lat, lon);
//
//        // 3.
//        return categoryManager.getContestants(user, location, categoryId);

    }
}
