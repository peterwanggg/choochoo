package com.pwang.kings.api;

import com.pwang.kings.objects.api.kings.GetMatchResponse;
import com.pwang.kings.objects.api.kings.NextContestantResponse;
import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.Auth;

import javax.annotation.Nullable;
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

    @Path("/{category-type}/category/{category-id}")
    @POST
    NextContestantResponse submit(
            @Auth KingsUser kingsUser,
            @NotNull @PathParam("category-type") String categoryType,
            @NotNull @PathParam("category-id") Long categoryId,
            @NotNull @QueryParam("winner-contestant-id") long winnerContestantId,
            @NotNull @QueryParam("loser-contestant-id") long loserContestantId,
            @NotNull @QueryParam("next-contestant-id") long nextContestantId);

    @Path("/{category-type}/category/{category-id}")
    @GET
    GetMatchResponse getMatchForCategory(
            @Auth KingsUser kingsUser,
            @NotNull @PathParam("category-type") String categoryType,
            @NotNull @PathParam("category-id") Long categoryId);

    @Path("/{category-type}/contestant/{contestant-id}")
    @GET
    GetMatchResponse getMatchForContestant(
            @Auth KingsUser kingsUser,
            @NotNull @PathParam("category-type") String categoryType,
            @NotNull @PathParam("contestant-id") Long contestantId,
            @Nullable @QueryParam("skip-contestant-id") Long skipContestantId);


}
