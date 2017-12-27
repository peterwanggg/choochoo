package com.pwang.kings.adapters.google;

import com.google.maps.model.PlacesSearchResult;
import com.pwang.kings.objects.model.Contestant;

/**
 * @author pwang on 12/26/17.
 */
public final class PlacesToContestantsAdapter implements GoogleAdapter<PlacesSearchResult, Contestant> {

    @Override
    public Contestant adapt(PlacesSearchResult apiObject) {
        return null;
    }
}
