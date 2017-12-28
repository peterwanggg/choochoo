package com.pwang.kings.adapters.zomato;

import com.pwang.kings.objects.api.zomato.Restaurant;
import com.pwang.kings.objects.model.ApiProviderType;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.ImmutableContestant;

import java.io.IOException;
import java.net.URL;

/**
 * @author pwang on 12/27/17.
 */
public final class RestaurantToContestantAdapter implements ZomatoAdapter<Restaurant, Contestant> {

    @Override
    public Contestant adapt(Restaurant apiObject) throws IOException {
        return ImmutableContestant.builder()
                .contestantName(apiObject.getRestaurantValue().getName())
                .imageUrl(new URL(apiObject.getRestaurantValue().getFeaturedImage()))
                .apiProviderType(ApiProviderType.zomato.toString())
                .apiProviderId(apiObject.getRestaurantValue().getId().toString())
                .build();
    }

}
