package com.pwang.kings.matchers;

import com.pwang.kings.categories.CategoryTypeManager;
import com.pwang.kings.db.daos.BoutDao;
import com.pwang.kings.db.daos.ContestantDao;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.objects.action.Bout;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.Location;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.Optional;

/**
 * @author pwang on 1/11/18.
 */
public final class PersonalizedContestantMatcher implements ContestantMatcher {

    private static Logger LOGGER = Logger.getLogger(PersonalizedContestantMatcher.class);

    private BoutDao boutDao;
    private ContestantDao contestantDao;
    private LocationDao locationDao;

    public PersonalizedContestantMatcher(
            BoutDao boutDao,
            ContestantDao contestantDao,
            LocationDao locationDao) {
        this.boutDao = boutDao;
        this.contestantDao = contestantDao;
        this.locationDao = locationDao;
    }

    @Override
    public Optional<Contestant> findNextMatch(
            Long kingsUserId, Contestant contestant, CategoryTypeManager categoryTypeManager) {
        // find next most popular
        Optional<Long> nextMostPopular = boutDao.getBestContestantFromBout(
                kingsUserId,
                contestant.getContestantId(),
                contestant.getCategoryId());
        if (nextMostPopular.isPresent()) {
            return Optional.of(contestantDao.getById(nextMostPopular.get()))
                    .orElseThrow(() ->
                            new WebApplicationException("inconsistent db", HttpStatus.INTERNAL_SERVER_ERROR_500));
        }

        // find next highest contestant id
        Optional<Long> nextUnseen = boutDao.getBestContestantFromContestant(
                kingsUserId,
                contestant.getContestantId(),
                contestant.getCategoryId());
        if (nextUnseen.isPresent()) {
            return Optional.of(contestantDao.getById(nextUnseen.get()))
                    .orElseThrow(() -> new WebApplicationException("inconsistent db", HttpStatus.INTERNAL_SERVER_ERROR_500));
        }

        // get from API
        Location location = locationDao.getByContestantId(contestant.getContestantId())
                .orElseThrow(() -> new WebApplicationException("inconsistent db", HttpStatus.INTERNAL_SERVER_ERROR_500));
        try {
            return categoryTypeManager.getNewContestantsFromApi(
                    location,
                    categoryTypeManager.getCategoriesByLocation(location.getLocationId()).get(contestant.getCategoryId()),
                    1).stream().findFirst();
        } catch (IOException e) {
            LOGGER.error("could not get contestants from API", e);
            return Optional.empty();
        }
    }

    @Override
    public Bout findNextBout(Long kingsUserId, Long categoryId) {
        return null;
    }
}
