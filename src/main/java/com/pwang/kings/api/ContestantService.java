package com.pwang.kings.api;

import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.Auth;

import javax.validation.constraints.NotNull;
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
            @Auth KingsUser kingsUser,
            @NotNull @QueryParam("lat") double lat,
            @NotNull @QueryParam("lon") double lon,
            @NotNull @QueryParam("category-id") long categoryId) throws IOException;


}
