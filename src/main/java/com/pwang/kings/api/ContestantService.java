package com.pwang.kings.api;

import com.pwang.kings.objects.api.kings.ContestantsResponse;
import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.Auth;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

/**
 * @author pwang on 12/26/17.
 */
@Path("/contestants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ContestantService {

    @Path("/category/{category-type}/{category-id}")
    @GET
    ContestantsResponse getContestantsForCategory(
            @NotNull @PathParam("category-type") String categoryType,
            @NotNull @PathParam("category-id") Long categoryId,
            @NotNull @QueryParam("location-id") Long locationId,
            @Nullable @QueryParam("page") Integer page);

    @Path("/search/{category-type}")
    @GET
    List<Contestant> searchByName(
            @Auth KingsUser kingsUser,
            @NotNull @QueryParam("lat") Double lat,
            @NotNull @QueryParam("lon") Double lon,
            @NotNull @PathParam("category-type") String categoryType,
            @NotNull @QueryParam("contestant-name") String contestantName);

    @Path("/skip/{category-id}/{contestant-id}")
    @POST
    Response addSkip(
            @Auth KingsUser kingsUser,
            @NotNull @PathParam("category-id") Long categoryId,
            @NotNull @PathParam("contestant-id") Long contestantId);

    @Path("/skip/{category-id}/{contestant-id}")
    @DELETE
    Response deleteSkip(
            @Auth KingsUser kingsUser,
            @NotNull @PathParam("category-id") Long categoryId,
            @NotNull @PathParam("contestant-id") Long contestantId);

    @Path("/skip/{category-id}")
    @GET
    Set<Long> getSkips(
            @Auth KingsUser kingsUser,
            @NotNull @PathParam("category-id") Long categoryId);

}
