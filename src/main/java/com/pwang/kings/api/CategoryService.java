package com.pwang.kings.api;

import com.pwang.kings.objects.api.kings.CategorySummaryApi;
import com.pwang.kings.objects.model.Category;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;

/**
 * @author pwang on 12/31/17.
 */

@Path("/categories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CategoryService {

    @GET
    @Path("/{category-type}")
    Collection<Category> getCategories(
            @NotNull @PathParam("category-type") String categoryType,
            @NotNull @QueryParam("lat") Double lat,
            @NotNull @QueryParam("lon") Double lon);


    @GET
    @Path("/{category-type}/top")
    public List<CategorySummaryApi> getTopCategoriesWithStats(
            @NotNull @PathParam("category-type") String categoryTypeStr,
            @NotNull @QueryParam("lat") Double lat,
            @NotNull @QueryParam("lon") Double lon,
            @Nullable @QueryParam("limit") Integer limit,
            @Nullable @QueryParam("contestants") Integer contestantsPerCategory);

}
