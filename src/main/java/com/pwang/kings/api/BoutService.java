package com.pwang.kings.api;

import com.pwang.kings.objects.api.kings.BoutHistoryResponse;
import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.Auth;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * @author pwang on 12/28/17.
 */
@Path("/bout")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BoutService {

    @POST
    Response submit(
            @Auth KingsUser kingsUser,
            @NotNull @QueryParam("category-id") Long categoryId,
            @NotNull @QueryParam("winner-contestant-id") long winnerContestantId,
            @NotNull @QueryParam("loser-contestant-id") long loserContestantId);

    @Path("/{category-id}")
    @GET
    BoutHistoryResponse getBoutHistory(
            @Auth KingsUser kingsUser,
            @NotNull @PathParam("category-id") Long categoryId);


}
