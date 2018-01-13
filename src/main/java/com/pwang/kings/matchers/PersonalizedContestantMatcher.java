package com.pwang.kings.matchers;

import com.pwang.kings.categories.CategoryTypeManager;
import com.pwang.kings.db.daos.BoutDao;
import com.pwang.kings.db.daos.ContestantDao;
import com.pwang.kings.db.daos.ContestantRankDao;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.objects.db.LongPair;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.stats.ContestantRank;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author pwang on 1/11/18.
 */
public final class PersonalizedContestantMatcher implements ContestantMatcher {

    private static Logger LOGGER = Logger.getLogger(PersonalizedContestantMatcher.class);

    private final BoutDao boutDao;
    private final ContestantDao contestantDao;
    private final ContestantRankDao contestantRankDao;
    private final LocationDao locationDao;

    public PersonalizedContestantMatcher(
            BoutDao boutDao,
            ContestantDao contestantDao,
            ContestantRankDao contestantRankDao, LocationDao locationDao) {
        this.boutDao = boutDao;
        this.contestantDao = contestantDao;
        this.contestantRankDao = contestantRankDao;
        this.locationDao = locationDao;
    }

    @Override
    public Optional<Contestant> findNextMatch(
            Long kingsUserId, Contestant contestant, CategoryTypeManager categoryTypeManager) {

        // find unseen contestant with most wins from this user
        Optional<Long> nextMostPopular = boutDao.getBestContestantFromBout(
                kingsUserId,
                contestant.getContestantId(),
                contestant.getCategoryId());
        if (nextMostPopular.isPresent()) {
            return Optional.of(getFromDbOrThrow(contestantDao, nextMostPopular.get()));
        }

        // find next highest contestant id
        Optional<Long> nextUnseen = boutDao.getBestContestantFromContestant(
                kingsUserId,
                contestant.getContestantId(),
                contestant.getCategoryId());
        if (nextUnseen.isPresent()) {
            return Optional.of(getFromDbOrThrow(contestantDao, nextUnseen.get()));
        }

        // get from API
        Location location = locationDao.getByContestantId(contestant.getContestantId())
                .orElseThrow(() -> new WebApplicationException("inconsistent db", HttpStatus.INTERNAL_SERVER_ERROR_500));
        try {
            return categoryTypeManager.getNewContestantsFromApi(
                    location,
                    categoryTypeManager.getCategoriesByLocation(location.getLocationId()).get(contestant.getCategoryId()),
                    1)
                    .stream()
                    .findFirst();
        } catch (IOException e) {
            LOGGER.error("could not get contestants from API", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Pair<Contestant, Contestant>> findNextBout(
            Long kingsUserId, Long categoryId, CategoryTypeManager categoryTypeManager) {

        // get this user's top 2 contestants (by most wins) that have not faced each other
        Optional<LongPair> bestMatch = boutDao.getBestNewMatch(kingsUserId, categoryId);
        if (bestMatch.isPresent()) {
            return Optional.of(
                    ImmutablePair.of(
                            getFromDbOrThrow(contestantDao, bestMatch.get().getLeft()),
                            getFromDbOrThrow(contestantDao, bestMatch.get().getRight())
                    ));
        }

        // get the most popular bout from this category across all users
        List<ContestantRank> topTwo = contestantRankDao.getByCategoryOrderByRank(categoryId, 2);
        if (topTwo.size() >= 2) {
            return Optional.of(
                    ImmutablePair.of(
                            getFromDbOrThrow(contestantDao, topTwo.get(0).getContestantId()),
                            getFromDbOrThrow(contestantDao, topTwo.get(1).getContestantId())
                    ));
        }

        // get the top 2 contestants in the category
        Location location = locationDao.getByCategoryId(categoryId)
                .orElseThrow(() -> new WebApplicationException("inconsistent db", HttpStatus.INTERNAL_SERVER_ERROR_500));
        try {
            List<Contestant> contestants = categoryTypeManager.getContestants(
                    location,
                    categoryId,
                    Optional.empty());
            if (contestants.size() >= 2) {
                return Optional.of(
                        ImmutablePair.of(
                                contestants.get(0),
                                contestants.get(1)
                        ));
            }
        } catch (IOException e) {
            LOGGER.error("could not get contestants from API", e);
        }

        return Optional.empty();
    }

    private static Contestant getFromDbOrThrow(ContestantDao contestantDao, Long contestantId) {
        return contestantDao.getById(contestantId)
                .orElseThrow(() -> new WebApplicationException("inconsistent db", HttpStatus.INTERNAL_SERVER_ERROR_500));
    }
}
