package com.pwang.kings.resources;

import com.pwang.kings.KingsConstants;
import com.pwang.kings.api.CategoryService;
import com.pwang.kings.categories.CategoryTypeManager;
import com.pwang.kings.categories.CategoryTypeManagerFactory;
import com.pwang.kings.db.daos.ContestantDao;
import com.pwang.kings.db.daos.ContestantRankDao;
import com.pwang.kings.db.daos.ContestantStatsDao;
import com.pwang.kings.objects.api.kings.CategorySummaryApi;
import com.pwang.kings.objects.api.kings.ImmutableCategorySummaryApi;
import com.pwang.kings.objects.api.kings.ImmutableContestantEntry;
import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.CategoryType;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.objects.stats.ContestantRank;
import com.pwang.kings.objects.stats.ContestantStats;
import com.pwang.kings.stats.ContestantStatsUtil;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.annotation.Nullable;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author pwang on 12/31/17.
 */
public final class CategoryResource implements CategoryService {

    Logger LOGGER = Logger.getLogger(ContestantResource.class);

    private final CategoryTypeManagerFactory categoryTypeManagerFactory;
    private final ContestantDao contestantDao;
    private final ContestantRankDao contestantRankDao;
    private final ContestantStatsDao contestantStatsDao;

    public CategoryResource(CategoryTypeManagerFactory categoryTypeManagerFactory,
                            ContestantDao contestantDao,
                            ContestantRankDao contestantRankDao,
                            ContestantStatsDao contestantStatsDao) {

        this.categoryTypeManagerFactory = categoryTypeManagerFactory;
        this.contestantDao = contestantDao;
        this.contestantRankDao = contestantRankDao;
        this.contestantStatsDao = contestantStatsDao;
    }

    @Override
    public Collection<Category> getCategories(String categoryTypeStr, Double lat, Double lon) {
        CategoryType categoryType = CategoryType.valueOf(categoryTypeStr);
        CategoryTypeManager categoryManager = categoryTypeManagerFactory.getCategoryManager(categoryType);

        try {
            Location location = categoryManager.getLocation(lat, lon)
                    .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

            return categoryManager.getCategoriesByLocation(location.getLocationId()).values();
        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Override
    public List<CategorySummaryApi> getTopCategoriesWithStats(String categoryTypeStr, Double lat, Double lon, @Nullable Integer limit, @Nullable Integer contestantsPerCategory) {
        CategoryType categoryType = CategoryType.valueOf(categoryTypeStr);
        CategoryTypeManager categoryManager = categoryTypeManagerFactory.getCategoryManager(categoryType);

        try {
            Location location = categoryManager.getLocation(lat, lon)
                    .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

            Stream<CategorySummaryApi> categorySummaryApiStream = categoryManager
                    .getTopCategoriesByLocation(location.getLocationId())
                    .stream()
                    .map(category -> {
                        List<ContestantRank> orderedRank = contestantRankDao.getByCategoryOrderByRank(
                                category.getCategoryId(),
                                Optional.ofNullable(contestantsPerCategory).orElse(KingsConstants.CONTESTANTS_PAGE_MIN_SIZE));
                        List<Long> orderedContestantIds = orderedRank.stream().map(ContestantRank::getContestantId).collect(Collectors.toList());

                        Map<Long, ContestantStats> statsMap = orderedContestantIds.isEmpty() ?
                                new HashMap<>() :
                                contestantStatsDao.getByIds(orderedContestantIds).stream().collect(Collectors.toMap(
                                        ContestantStats::getContestantId,
                                        Function.identity()));
                        Map<Long, Contestant> contestants = orderedContestantIds.isEmpty() ?
                                new HashMap<>() :
                                contestantDao.getByIds(orderedContestantIds).stream().collect(Collectors.toMap(
                                        Contestant::getContestantId,
                                        Function.identity()));

                        return ImmutableCategorySummaryApi.builder()
                                .category(category)
                                .addAllContestantEntries(orderedRank.stream().map(rank -> {
                                    Contestant contestant = contestants.get(rank.getContestantId());
                                    if (contestant == null) {
                                        return null;
                                    }
                                    return ImmutableContestantEntry.builder()
                                            .contestant(contestant)
                                            .contestantStats(ContestantStatsUtil.fromDb(
                                                    statsMap.get(contestant.getContestantId()),
                                                    rank))
                                            .build();
                                }).filter(Objects::nonNull).collect(Collectors.toList()))
                                .build();
                    });

            if (limit == null) {
                return categorySummaryApiStream.collect(Collectors.toList());
            }
            return categorySummaryApiStream.limit(limit).collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }
}

