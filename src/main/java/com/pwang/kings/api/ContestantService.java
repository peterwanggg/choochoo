package com.pwang.kings.api;

import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.User;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
            @QueryParam("lat") double lat,
            @QueryParam("lon") double lon,
            @QueryParam("category-id") int categoryId);




}
