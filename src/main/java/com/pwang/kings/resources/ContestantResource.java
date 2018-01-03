package com.pwang.kings.resources;

import com.pwang.kings.api.ContestantService;
import com.pwang.kings.categories.CategoryManager;
import com.pwang.kings.categories.CategoryManagerFactory;
import com.pwang.kings.db.daos.CategoryDao;
import com.pwang.kings.db.daos.ContestantDao;
import com.pwang.kings.objects.model.*;
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
    private final ContestantDao contestantDao;

    private final int SEARCH_CONTESTANTS_SIZE_LIMIT = 20;

    public ContestantResource(
            ContestantDao contestantDao,
            CategoryDao categoryDao,
            CategoryManagerFactory categoryManagerFactory) {
        this.contestantDao = contestantDao;
        this.categoryDao = categoryDao;
        this.categoryManagerFactory = categoryManagerFactory;
    }


    @Override
    public List<Contestant> getContestantsForChallenger(
            KingsUser kingsUser,
            Double lat,
            Double lon,
            Long challengerContestantId,
            Integer page
    ) {
        // 1. get challenger contestant
        Contestant challenger = contestantDao.getById(challengerContestantId).orElseThrow(() ->
                new WebApplicationException("invalid challenger-contestant-id: " + challengerContestantId, HttpStatus.BAD_REQUEST_400));

        // 2. get category and manager
        Category category = categoryDao.getById(challenger.getCategoryId())
                .orElseThrow(() ->
                        new WebApplicationException("could not find categoryId " + challenger.getCategoryId(), HttpStatus.BAD_REQUEST_400));
        CategoryManager categoryManager = categoryManagerFactory.getCategoryManager(category.getCategoryType());

        try {
            // 3. get location
            Location location = categoryManager.getLocation(lat, lon)
                    .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

            // 4. get contestants
            return categoryManager.getChallengers(
                    kingsUser,
                    location,
                    category,
                    challenger,
                    Optional.ofNullable(page));
        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public List<Contestant> getContestantsForCategory(
            KingsUser kingsUser,
            Double lat,
            Double lon,
            Long categoryId,
            Integer page
    ) {
        // 1. get category and manager
        Category category = categoryDao.getById(categoryId)
                .orElseThrow(() ->
                        new WebApplicationException("could not find categoryId " + categoryId, HttpStatus.BAD_REQUEST_400));
        CategoryManager categoryManager = categoryManagerFactory.getCategoryManager(category.getCategoryType());

        try {
            // 2. get location
            Location location = categoryManager.getLocation(lat, lon)
                    .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

            // 3. get contestants
            return categoryManager.getContestants(
                    kingsUser,
                    location,
                    category,
                    Optional.ofNullable(page));
        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    // TODO: rate limit
    @Override
    public List<Contestant> searchByName(KingsUser kingsUser, Double lat, Double lon, String categoryTypeStr, String contestantName) {
        CategoryType categoryType = CategoryType.valueOf(categoryTypeStr);
        CategoryManager categoryManager = categoryManagerFactory.getCategoryManager(categoryType);

        try {
            Location location = categoryManager.getLocation(lat, lon)
                    .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

            return categoryManager.searchContestants(location, contestantName);
        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

}
