package com.pwang.kings.resources;

import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;
import com.pwang.kings.api.ContestantService;
import com.pwang.kings.clients.ZomatoService;
import com.pwang.kings.objects.api.zomato.SearchResult;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.helloworld.core.User;
import retrofit2.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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




    @GET
    @Path("/search")
    public SearchResult search(
            @QueryParam("lat") double lat,
            @QueryParam("lon") double lon) throws IOException {

        Response<SearchResult> response = zomatoService.search(lat, lon).execute();
        if (response.isSuccessful()) {
            return response.body();
        }

        System.out.println("response:" + response.toString());

        throw new WebApplicationException(response.message(), response.code());
    }

    @Override
    public List<Contestant> getContestants(User user, double lat, double lon, int categoryId) {
        return null;
    }
}
