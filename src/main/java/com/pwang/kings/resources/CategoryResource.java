package com.pwang.kings.resources;

import com.pwang.kings.api.CategoryService;
import com.pwang.kings.categories.CategoryManager;
import com.pwang.kings.categories.CategoryManagerFactory;
import com.pwang.kings.db.daos.CategoryDao;
import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.CategoryType;
import com.pwang.kings.objects.model.KingsUser;
import com.pwang.kings.objects.model.Location;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;

/**
 * @author pwang on 12/31/17.
 */
public final class CategoryResource implements CategoryService {

    Logger LOGGER = Logger.getLogger(ContestantResource.class);

    private final CategoryManagerFactory categoryManagerFactory;
    private final CategoryDao categoryDao;

    public CategoryResource(
            CategoryDao categoryDao,
            CategoryManagerFactory categoryManagerFactory) {

        this.categoryDao = categoryDao;
        this.categoryManagerFactory = categoryManagerFactory;
    }

    @Override
    public List<Category> getCategories(KingsUser kingsUser, Double lat, Double lon, String categoryTypeStr) throws IOException {
        CategoryType categoryType = CategoryType.valueOf(categoryTypeStr);
        CategoryManager categoryManager = categoryManagerFactory.getCategoryManager(categoryType);

        Location location = categoryManager.getLocation(lat, lon)
                .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

        return categoryDao.getByLocationCategoryType(location.getLocationId(), categoryType.toString());
    }
}
