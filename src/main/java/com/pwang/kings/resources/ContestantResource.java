package com.pwang.kings.resources;

import com.pwang.kings.api.ContestantService;
import com.pwang.kings.categories.CategoryManager;
import com.pwang.kings.categories.CategoryManagerFactory;
import com.pwang.kings.db.daos.CategoryDao;
import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.KingsUser;
import com.pwang.kings.objects.model.Location;
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

    private final CategoryManagerFactory categoryManagerFactory;
    private final CategoryDao categoryDao;

    public ContestantResource(
            CategoryDao categoryDao,
            CategoryManagerFactory categoryManagerFactory) {
        this.categoryDao = categoryDao;
        this.categoryManagerFactory = categoryManagerFactory;
    }


    @Override
    public List<Contestant> getContestants(KingsUser kingsUser, double lat, double lon, long categoryId) {
        // 1. get category and manager
        Category category = categoryDao.getById(categoryId)
                .orElseThrow(() ->
                        new WebApplicationException("could not find categoryId " + categoryId, HttpStatus.BAD_REQUEST_400));
        LOGGER.info("Got category: " + category);
        CategoryManager categoryManager = categoryManagerFactory.getCategoryManager(category.getCategoryType());

        try {
            // 2. get location
            Optional<Location> location = categoryManager.getLocation(lat, lon);

            // 3. get contestants
            return categoryManager.getContestants(
                    kingsUser,
                    location.orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501)),
                    category);
        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }


    }
}
