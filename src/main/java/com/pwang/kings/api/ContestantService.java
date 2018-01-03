package com.pwang.kings.api;

import com.pwang.kings.objects.model.Contestant;
import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.Auth;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
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

    @Path("/challenger")
    @GET
    List<Contestant> getContestantsForChallenger(
            @Auth KingsUser kingsUser,
            @NotNull @QueryParam("lat") Double lat,
            @NotNull @QueryParam("lon") Double lon,
            @NotNull @QueryParam("challenger-contestant-id") Long contestantId,
            @Nullable @QueryParam("offset") Integer offset);

    @Path("/category")
    @GET
    List<Contestant> getContestantsForCategory(
            @Auth KingsUser kingsUser,
            @NotNull @QueryParam("lat") Double lat,
            @NotNull @QueryParam("lon") Double lon,
            @NotNull @QueryParam("category-id") Long categoryId,
            @Nullable @QueryParam("offset") Integer offset);

    @Path("/search")
    @GET
    List<Contestant> searchByName(
            @Auth KingsUser kingsUser,
            @NotNull @QueryParam("lat") Double lat,
            @NotNull @QueryParam("lon") Double lon,
            @NotNull @QueryParam("category-type") String categoryType,
            @NotNull @QueryParam("contestant-name") String contestantName);


}
