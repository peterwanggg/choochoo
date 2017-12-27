package com.pwang.clashbash.api;

import com.google.maps.model.LatLng;
import com.pwang.clashbash.objects.api.zomato.SearchResult;
import com.pwang.clashbash.objects.model.Contestant;
import com.pwang.helloworld.core.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * @author pwang on 12/26/17.
 */
@Path("/contestants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ContestantService {

    @GET
    List<Contestant> getContestants(
            @Auth User user,
            @QueryParam("location") LatLng latLng,
            @QueryParam("category-id") int categoryId);

    @GET
    @Path("/search")
    SearchResult search(@QueryParam("lat") double lat, @QueryParam("lon") double lon) throws IOException;


}
