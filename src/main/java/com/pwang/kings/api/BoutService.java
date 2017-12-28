package com.pwang.kings.api;

import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author pwang on 12/28/17.
 */
@Path("/bout")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BoutService {

    @POST
    void submit(
            @Auth KingsUser kingsUser,
            @QueryParam("winner-contestant-id") long winnerContestantId,
            @QueryParam("loser-contestant-id") long loserContestantId);
}
