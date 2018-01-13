package com.pwang.kings.resources;

import com.google.common.collect.ImmutableList;
import com.pwang.kings.api.BoutService;
import com.pwang.kings.categories.CategoryTypeManager;
import com.pwang.kings.categories.CategoryTypeManagerFactory;
import com.pwang.kings.db.daos.*;
import com.pwang.kings.matchers.ContestantMatcher;
import com.pwang.kings.matchers.PersonalizedContestantMatcher;
import com.pwang.kings.objects.action.Bout;
import com.pwang.kings.objects.action.ImmutableBout;
import com.pwang.kings.objects.api.kings.*;
import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.KingsUser;
import com.pwang.kings.stats.ContestantStatsUtil;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.util.Optional;

/**
 * @author pwang on 12/28/17.
 */
public final class BoutResource implements BoutService {

    private static Logger LOGGER = Logger.getLogger(BoutResource.class);

    private final CategoryTypeManagerFactory categoryTypeManagerFactory;
    private final ContestantMatcher contestantMatcher;

    private final BoutDao boutDao;
    private final CategoryDao categoryDao;
    private final ContestantStatsDao contestantStatsDao;
    private final ContestantRankDao contestantRankDao;
    private final ContestantDao contestantDao;


    public BoutResource(
            CategoryTypeManagerFactory categoryTypeManagerFactory,
            BoutDao boutDao,

            CategoryDao categoryDao, ContestantStatsDao contestantStatsDao,
            ContestantRankDao contestantRankDao,
            ContestantDao contestantDao,
            LocationDao locationDao) {
        this.categoryTypeManagerFactory = categoryTypeManagerFactory;

        this.boutDao = boutDao;
        this.categoryDao = categoryDao;

        this.contestantMatcher = new PersonalizedContestantMatcher(boutDao, contestantDao, locationDao);
        this.contestantStatsDao = contestantStatsDao;
        this.contestantRankDao = contestantRankDao;
        this.contestantDao = contestantDao;
    }

    @Override
    public SubmitBoutResponse submit(
            KingsUser kingsUser,
            Long categoryId,
            long winnerContestantId,
            long loserContestantId,
            long nextContestantId) {

        // get contestant/category/categoryManager
        Contestant nextContestant = contestantDao.getById(nextContestantId).orElseThrow(
                () -> new WebApplicationException("could not find contestant " + nextContestantId, HttpStatus.BAD_REQUEST_400));
        Category category = categoryDao.getById(nextContestant.getCategoryId())
                .orElseThrow(() -> new WebApplicationException("could not find categoryId " + nextContestant.getCategoryId(), HttpStatus.BAD_REQUEST_400));
        CategoryTypeManager categoryTypeManager = categoryTypeManagerFactory.getCategoryManager(category.getCategoryType());

        // create and submit bout
        Bout bout = ImmutableBout.builder()
                .categoryId(categoryId)
                .winnerContestantId(winnerContestantId)
                .loserContestantId(loserContestantId)
                .kingsUserId(kingsUser.getKingsUserId())
                .build();
        boutDao.create(bout);

        // find next contestant
        Optional<ContestantEntry> matchEntry = Optional.empty();
        Optional<Contestant> match = contestantMatcher.findNextMatch(
                kingsUser.getKingsUserId(),
                nextContestant,
                categoryTypeManager);
        if (match.isPresent()) {
            matchEntry = Optional.of(
                    ContestantStatsUtil.fetchAndJoinContestantStats(
                            ImmutableList.of(match.get()),
                            contestantStatsDao,
                            contestantRankDao
                    ).get(0)
            );
        }
        return ImmutableSubmitBoutResponse.builder()
                .nextContestant(matchEntry)
                .build();

    }

    @Override
    public GetBoutResponse getNextBout(KingsUser kingsUser, Long categoryId) {
        return ImmutableGetBoutResponse.builder()
                .bout(contestantMatcher.findNextBout(
                        kingsUser.getKingsUserId(), categoryId))
                .build();
    }

}
