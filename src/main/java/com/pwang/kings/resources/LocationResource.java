package com.pwang.kings.resources;

import com.pwang.kings.api.LocationService;
import com.pwang.kings.categories.CategoryTypeManager;
import com.pwang.kings.categories.CategoryTypeManagerFactory;
import com.pwang.kings.objects.model.CategoryType;
import com.pwang.kings.objects.model.Location;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;

/**
 * @author pwang on 1/6/18.
 */

public final class LocationResource implements LocationService {

    Logger LOGGER = Logger.getLogger(LocationResource.class);

    private final CategoryTypeManagerFactory categoryTypeManagerFactory;

    public LocationResource(CategoryTypeManagerFactory categoryTypeManagerFactory) {
        this.categoryTypeManagerFactory = categoryTypeManagerFactory;
    }

    @Override
    public Location getLocation(String categoryTypeStr, Double lat, Double lon) {
        CategoryType categoryType = CategoryType.valueOf(categoryTypeStr);
        CategoryTypeManager categoryManager = categoryTypeManagerFactory.getCategoryManager(categoryType);

        try {
            return categoryManager.getLocation(lat, lon)
                    .orElseThrow(() -> new WebApplicationException("unsupported location", HttpStatus.NOT_IMPLEMENTED_501));

        } catch (IOException e) {
            LOGGER.error("api exception", e);
            throw new WebApplicationException("could not interact with the dependent API", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }
}
