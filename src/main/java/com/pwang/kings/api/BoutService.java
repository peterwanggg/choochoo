package com.pwang.kings.api;

import com.pwang.kings.objects.api.kings.GetBoutResponse;
import com.pwang.kings.objects.api.kings.SubmitBoutResponse;
import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.Auth;

import javax.validation.constraints.NotNull;
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
    SubmitBoutResponse submit(
            @Auth KingsUser kingsUser,
            @NotNull @QueryParam("category-id") Long categoryId,
            @NotNull @QueryParam("winner-contestant-id") long winnerContestantId,
            @NotNull @QueryParam("loser-contestant-id") long loserContestantId,
            @NotNull @QueryParam("next-contestant-id") long nextContestantId);

    @Path("/category/{category-id}")
    @GET
    GetBoutResponse getNextBout(
            @Auth KingsUser kingsUser,
            @NotNull @PathParam("category-id") Long categoryId);

}
