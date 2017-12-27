package com.pwang.clashbash.adapters;

import com.google.maps.model.PlacesSearchResponse;
import com.pwang.clashbash.objects.model.Contestant;

import java.util.List;

/**
 * @author pwang on 12/26/17.
 */
public final class PlacesToContestantsAdapter implements GoogleAdapter<PlacesSearchResponse, List<Contestant>> {
    @Override
    public List<Contestant> adapt(PlacesSearchResponse googleObject) {

//        return Arrays.stream(googleObject.results).map(
//
//        )

        return null;

    }
}
