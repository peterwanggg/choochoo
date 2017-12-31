package com.pwang.kings.api;

import com.pwang.kings.objects.model.Category;
import com.pwang.kings.objects.model.KingsUser;
import io.dropwizard.auth.Auth;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collection;

/**
 * @author pwang on 12/31/17.
 */

@Path("/categories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CategoryService {

    @GET
    Collection<Category> getCategories(
            @Auth KingsUser kingsUser,
            @NotNull @QueryParam("lat") Double lat,
            @NotNull @QueryParam("lon") Double lon,
            @NotNull @QueryParam("category-type") String categoryType) throws IOException;

}
