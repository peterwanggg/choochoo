package com.pwang.kings.api;

import com.pwang.kings.objects.model.Location;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author pwang on 1/6/18.
 */
@Path("/location")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface LocationService {

    @Path("/{category-type}/{lat}/{lon}")
    @GET
    Location getLocation(
            @NotNull @PathParam("category-type") String categoryType,
            @NotNull @PathParam("lat") Double lat,
            @NotNull @PathParam("lon") Double lon);
}
