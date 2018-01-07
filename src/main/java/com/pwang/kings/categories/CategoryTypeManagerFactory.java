package com.pwang.kings.categories;

import com.google.common.collect.ImmutableMap;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.db.daos.CategoryDao;
import com.pwang.kings.db.daos.ContestantDao;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.objects.model.CategoryType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author pwang on 12/26/17.
 */
public class CategoryTypeManagerFactory {

    private final Map<CategoryType, CategoryTypeManager> categoryTypeManagers;

    public CategoryTypeManagerFactory(
            ZomatoService zomatoService,
            LocationDao locationDao,
            CategoryDao categoryDao,
            ContestantDao contestantDao,
            Optional<Long> cityIdOverride
    ) {
        categoryTypeManagers = ImmutableMap.of(
                CategoryType.restaurant, new RestaurantCategoryManager(
                        zomatoService,
                        locationDao,
                        categoryDao,
                        contestantDao,
                        cityIdOverride)
        );
    }

    public CategoryTypeManager getCategoryManager(CategoryType categoryType) {
        return categoryTypeManagers.get(categoryType);
    }

    public Collection<CategoryTypeManager> getAllCategoryManagers() {
        return categoryTypeManagers.values();
    }
}
