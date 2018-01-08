package com.pwang.kings.resources;

import com.google.common.collect.ImmutableList;
import com.pwang.kings.api.ContestantService;
import com.pwang.kings.categories.CategoryTypeManager;
import com.pwang.kings.categories.CategoryTypeManagerFactory;
import com.pwang.kings.db.daos.*;
import com.pwang.kings.objects.api.kings.ChallengerResponse;
import com.pwang.kings.objects.api.kings.ContestantsResponse;
import com.pwang.kings.objects.api.kings.ImmutableChallengerResponse;
import com.pwang.kings.objects.api.kings.ImmutableContestantsResponse;
import com.pwang.kings.objects.model.*;
import com.pwang.kings.stats.ContestantStatsUtil;
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

    private final CategoryTypeManagerFactory categoryTypeManagerFactory;
    private final CategoryDao categoryDao;
    private final ContestantDao contestantDao;
    private final ContestantStatsDao contestantStatsDao;
    private final ContestantRankDao contestantRankDao;
    private final BoutDao boutDao;

    private final int SEARCH_CONTESTANTS_SIZE_LIMIT = 20;

    public ContestantResource(
            ContestantDao contestantDao,
            CategoryDao categoryDao,
            CategoryTypeManagerFactory categoryTypeManagerFactory,
            ContestantStatsDao contestantStatsDao,
            ContestantRankDao contestantRankDao,
            BoutDao boutDao) {
        this.contestantDao = contestantDao;
        this.categoryDao = categoryDao;
        this.categoryTypeManagerFactory = categoryTypeManagerFactory;
        this.contestantStatsDao = contestantStatsDao;
        this.contestantRankDao = contestantRankDao;
        this.boutDao = boutDao;
    }


    @Override
    public ChallengerResponse getContestantsForChallenger(
            KingsUser kingsUser,
            Double lat,
            Double lon,
            Long challengerContestantId,
            Integer offset
    ) {
        // 1. get challenger contestant
        Contestant challenger = contestantDao.getById(challengerContestantId).orElseThrow(() ->
                new WebApplicationException("invalid challenger-contestant-id: " + challengerContestantId, HttpStatus.BAD_REQUEST_400));

        // 2. get category and manager
        Category category = categoryDao.getById(challenger.getCategoryId())
                .orElseThrow(() ->
                        new WebApplicationException("could not find categoryId " + challenger.getCategoryId(), HttpStatus.BAD_REQUEST_400));
        CategoryTypeManager categoryManager = categoryTypeManagerFactory.getCategoryManager(category.getCategoryType());

        try {
            // 3. get location
            Location location = categoryManager.getLocation(lat, lon)
                    .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

            // 4. get contestants and status
            return ImmutableChallengerResponse.builder()
                    .addAllContestants(
                            ContestantStatsUtil.fetchAndJoinContestantStatsAndBouts(
                                    kingsUser.getKingsUserId(),
                                    categoryManager.getChallengers(
                                            kingsUser,
                                            location,
                                            category,
                                            challenger,
                                            Optional.ofNullable(offset)),
                                    contestantStatsDao,
                                    contestantRankDao,
                                    boutDao))
                    .challenger(
                            ContestantStatsUtil.fetchAndJoinContestantStatsAndBouts(
                                    kingsUser.getKingsUserId(),
                                    ImmutableList.of(challenger),
                                    contestantStatsDao,
                                    contestantRankDao,
                                    boutDao).stream().findFirst()
                                    .orElseThrow(() ->
                                            new WebApplicationException("invalid challenger-contestant-id: " + challengerContestantId, HttpStatus.BAD_REQUEST_400)))
                    .build();
        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public ContestantsResponse getContestantsForCategory(
            KingsUser kingsUser,
            Double lat,
            Double lon,
            Long categoryId,
            Integer offset
    ) {
        // 1. get category and manager
        Category category = categoryDao.getById(categoryId)
                .orElseThrow(() ->
                        new WebApplicationException("could not find categoryId " + categoryId, HttpStatus.BAD_REQUEST_400));
        CategoryTypeManager categoryManager = categoryTypeManagerFactory.getCategoryManager(category.getCategoryType());

        try {
            // 2. get location
            Location location = categoryManager.getLocation(lat, lon)
                    .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

            // 3. get contestants
            return ImmutableContestantsResponse.builder()
                    .contestants(
                            ContestantStatsUtil.fetchAndJoinContestantStatsAndBouts(
                                    kingsUser.getKingsUserId(),
                                    categoryManager.getContestants(
                                            kingsUser,
                                            location,
                                            category,
                                            Optional.ofNullable(offset)),
                                    contestantStatsDao,
                                    contestantRankDao,
                                    boutDao))
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

}
