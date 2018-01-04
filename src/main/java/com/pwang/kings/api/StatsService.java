package com.pwang.kings.api;

import com.pwang.kings.objects.model.KingsUser;
import com.pwang.kings.objects.stats.ContestantStats;
import io.dropwizard.auth.Auth;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * @author pwang on 1/3/18.
 */
@Path("/stats")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StatsService {

    @Path("/contestant/{contestant-id}")
    @GET
    ContestantStats getContestantStats(
            @Auth KingsUser kingsUser,
            @NotNull @PathParam("contestant-id") Long contestantId) throws IOException;

}
