package com.pwang.kings.resources;

import com.google.common.collect.ImmutableList;
import com.pwang.kings.api.ContestantService;
import com.pwang.kings.categories.CategoryTypeManager;
import com.pwang.kings.categories.CategoryTypeManagerFactory;
import com.pwang.kings.db.daos.ContestantRankDao;
import com.pwang.kings.db.daos.ContestantSkipsDao;
import com.pwang.kings.db.daos.ContestantStatsDao;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.objects.api.kings.ContestantsResponse;
import com.pwang.kings.objects.api.kings.ImmutableContestantsResponse;
import com.pwang.kings.objects.model.*;
import com.pwang.kings.stats.ContestantStatsUtil;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.annotation.Nullable;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


/**
 * @author pwang on 12/26/17.
 */
public final class ContestantResource implements ContestantService {

    Logger LOGGER = Logger.getLogger(ContestantResource.class);

    private final CategoryTypeManagerFactory categoryTypeManagerFactory;
    private final ContestantSkipsDao contestantSkipsDao;
    private final ContestantStatsDao contestantStatsDao;
    private final ContestantRankDao contestantRankDao;
    private final LocationDao locationDao;


    public ContestantResource(
            CategoryTypeManagerFactory categoryTypeManagerFactory,

            ContestantSkipsDao contestantSkipsDao,
            ContestantStatsDao contestantStatsDao,
            ContestantRankDao contestantRankDao,
            LocationDao locationDao) {
        this.categoryTypeManagerFactory = categoryTypeManagerFactory;
        this.contestantSkipsDao = contestantSkipsDao;
        this.contestantStatsDao = contestantStatsDao;
        this.contestantRankDao = contestantRankDao;
        this.locationDao = locationDao;
    }

    @Override
    public ContestantsResponse getContestantsForCategory(
            String categoryType, Long categoryId, Long locationId, @Nullable Integer page) {
        // 1. get category and manager
        CategoryTypeManager categoryManager = categoryTypeManagerFactory.getCategoryManager(
                CategoryType.valueOf(categoryType));

        try {
            // 2. get location
            Location location = locationDao.getById(locationId)
                    .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

            // 3. get contestants
            return ImmutableContestantsResponse.builder()
                    .contestants(
                            ContestantStatsUtil.fetchAndJoinContestantStats(
                                    categoryManager.getContestants(
                                            location,
                                            categoryId,
                                            Optional.ofNullable(page)),
                                    contestantStatsDao,
                                    contestantRankDao))
                    .build();
        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    // TODO: rate limit
    @Override
    public List<Contestant> searchByName(KingsUser kingsUser, Double lat, Double lon, String categoryTypeStr, String contestantName) {
        CategoryType categoryType = CategoryType.valueOf(categoryTypeStr);
        CategoryTypeManager categoryManager = categoryTypeManagerFactory.getCategoryManager(categoryType);

        try {
            Location location = categoryManager.getLocation(lat, lon)
                    .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

            return categoryManager.searchContestants(location, contestantName);
        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public Response addSkip(KingsUser kingsUser, Long categoryId, Long contestantId) {
        contestantSkipsDao.addById(kingsUser.getKingsUserId(), categoryId, contestantId);
        return Response.ok().build();
    }

    @Override
    public Response deleteSkip(KingsUser kingsUser, Long categoryId, Long contestantId) {
        contestantSkipsDao.removeById(kingsUser.getKingsUserId(), categoryId, contestantId);
        return Response.ok().build();
    }


}
