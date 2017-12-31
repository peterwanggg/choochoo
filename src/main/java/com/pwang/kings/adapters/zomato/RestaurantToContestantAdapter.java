package com.pwang.kings.adapters.zomato;

import com.pwang.kings.objects.api.zomato.Restaurant;
import com.pwang.kings.objects.model.ApiProviderType;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.ImmutableContestant;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * @author pwang on 12/27/17.
 */
public final class RestaurantToContestantAdapter implements ZomatoAdapter<Restaurant, Contestant> {

    private static final String NOT_FOUND_CUISINE_NAME = "n/a";
    private static final Logger LOGGER = Logger.getLogger(RestaurantToContestantAdapter.class);

    @Override
    public Contestant adapt(Restaurant apiObject) {
        return ImmutableContestant.builder()
                .contestantName(apiObject.getRestaurant().getName())
                .imageUrl(getUrl(apiObject.getRestaurant().getFeaturedImage()))
                .apiProviderType(ApiProviderType.zomato.toString())
                .apiProviderId(apiObject.getRestaurant().getId().toString())
                .build();
    }

    public static String getCuisine(Optional<String> cuisines) {
        return cuisines.map(cuisinesStrArray -> cuisinesStrArray.split(",")[0])
                .orElse(NOT_FOUND_CUISINE_NAME);
    }

    private static URL getUrl(Optional<String> urlString) {
        if (!urlString.isPresent()) {
            return getDefaultUrl();
        }
        try {
            return new URL(urlString.get());
        } catch (MalformedURLException e) {
            LOGGER.warn("could not parse given url:" + urlString);
            return getDefaultUrl();
        }
    }

    private static URL getDefaultUrl() {
        try {
            return new URL("https://www.shareicon.net/data/512x512/2016/09/23/834003_fork_512x512.png");
        } catch (MalformedURLException e) {
            LOGGER.error("could not parse DEFAULT url", e);
            return null;
        }
    }


}
