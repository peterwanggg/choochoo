package com.pwang.kings.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.pwang.kings.categories.CategoryManager;
import com.pwang.kings.db.daos.LocationDao;
import com.pwang.kings.objects.model.ApiProviderType;
import com.pwang.kings.objects.model.CategoryType;
import com.pwang.kings.objects.model.Location;
import com.pwang.kings.serde.ObjectMappers;
import io.dropwizard.servlets.tasks.PostBodyTask;
import org.apache.log4j.Logger;

import javax.xml.ws.WebServiceException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * @author pwang on 12/27/17.
 */
public class InitializeCategoryLocationTask extends PostBodyTask {

    Logger LOGGER = Logger.getLogger(InitializeCategoryLocationTask.class);

    private final LocationDao locationDao;
    private final CategoryManager categoryManager;

    public InitializeCategoryLocationTask(
            LocationDao locationDao,
            CategoryType categoryType,
            CategoryManager categoryManager) {

        super("init_" + categoryType.toString());
        this.locationDao = locationDao;
        this.categoryManager = categoryManager;
    }


    @Override
    public void execute(ImmutableMultimap<String, String> parameters, String body, PrintWriter output) throws Exception {
        JsonNode locationRequest = ObjectMappers.RETROFIT_MAPPER.readTree(body);

        ApiProviderType apiProviderType = ApiProviderType.valueOf(locationRequest.get("api_provider_type").textValue());
        String apiProviderId = locationRequest.get("api_provider_id").toString();

        // get location
        Optional<Location> locationOptional = locationDao.getByApiId(apiProviderType.toString(), apiProviderId);
        Location location;
        if (locationOptional.isPresent()) {
            location = locationOptional.get();
        } else {
            location = categoryManager.getLocations(ImmutableList.of(apiProviderId))
                    .stream().findFirst()
                    .orElseThrow(() -> new WebServiceException("could not find location"));
        }

        categoryManager.populateCategory(location)
                .forEach(category -> LOGGER.info("Created category: " + category));

    }
}