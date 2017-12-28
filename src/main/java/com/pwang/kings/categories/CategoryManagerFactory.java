package com.pwang.kings.categories;

import com.google.common.collect.ImmutableMap;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.db.daos.CategoryDao;
import com.pwang.kings.db.daos.ContestantDao;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.objects.model.CategoryType;

import java.util.Collection;
import java.util.Map;

/**
 * @author pwang on 12/26/17.
 */
public class CategoryManagerFactory {

    private final Map<CategoryType, CategoryManager> categoryManagers;

    public CategoryManagerFactory(
            ZomatoService zomatoService,
            LocationDao locationDao,
            CategoryDao categoryDao,
            ContestantDao contestantDao
    ) {
        categoryManagers = ImmutableMap.of(
                CategoryType.restaurant, new RestaurantCategoryManager(
                        zomatoService,
                        locationDao,
                        categoryDao,
                        contestantDao)
        );
    }

    public CategoryManager getCategoryManager(CategoryType categoryType) {
        return categoryManagers.get(categoryType);
    }

    public Collection<CategoryManager> getAllCategoryManagers() {
        return categoryManagers.values();
    }
}
