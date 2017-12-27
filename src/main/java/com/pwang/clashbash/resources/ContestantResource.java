package com.pwang.clashbash.resources;

import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;
import com.pwang.clashbash.api.ContestantService;
import com.pwang.clashbash.clients.ZomatoService;
import com.pwang.clashbash.objects.api.zomato.SearchResult;
import com.pwang.clashbash.objects.model.Contestant;
import com.pwang.helloworld.core.User;
import retrofit2.Response;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;

/**
 * @author pwang on 12/26/17.
 */
public final class ContestantResource implements ContestantService {

    private final GeoApiContext googleContext;
    private final ZomatoService zomatoService;

    public ContestantResource(GeoApiContext googleContext, ZomatoService zomatoService) {
        this.googleContext = googleContext;
        this.zomatoService = zomatoService;
    }

//    @GET
//    public PlacesSearchResponse getContestants(@QueryParam("location") final LatLng latLng) throws InterruptedException, ApiException, IOException {
//
//        return PlacesApi.nearbySearchQuery(googleContext, latLng).radius(5000).await();
//    }

    @Override
    public List<Contestant> getContestants(User user, LatLng latLng, int categoryId) {
        return null;
    }


    // TODO: use callback
    @Override
    public SearchResult search(double lat, double lon) throws IOException {
        Response<SearchResult> response = zomatoService.search(lat, lon).execute();
        if (response.isSuccessful()) {
            return response.body();
        }

        System.out.println("response:" + response.toString());

        throw new WebApplicationException(response.message(), response.code());

    }

}
