package com.pwang.clashbash.resources;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pwang on 12/26/17.
 */
@Path("/contestants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContestantsResource {

    private final GeoApiContext googleContext;

    public ContestantsResource(GeoApiContext googleContext) {
        this.googleContext = googleContext;
    }

    @GET
    public PlacesSearchResponse getContestants(@QueryParam("location") final String coordinates) throws InterruptedException, ApiException, IOException {
        LatLng latlng;
        try {
            List<Double> cords = Arrays.stream(coordinates.split(",")).map(Double::parseDouble).collect(Collectors.toList());
            latlng = new LatLng(cords.get(0), cords.get(1));
        } catch (Exception e){
            throw new WebApplicationException(new IllegalArgumentException("could not parse location"),
                    Response.Status.BAD_REQUEST);
        }
        return PlacesApi.nearbySearchQuery(googleContext, latlng).radius(5000).await();
    }
}
