package com.pwang.kings.clients;

import com.pwang.kings.objects.api.zomato.CitiesResult;
import com.pwang.kings.objects.api.zomato.CuisinesResult;
import com.pwang.kings.objects.api.zomato.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * @author pwang on 12/26/17.
 */

public interface ZomatoService {

    // changing count dones't seem to change this
    int SEARCH_PAGE_SIZE = 20;

    @GET("/api/v2.1/search")
    Call<SearchResult> search(
            @Query("entity_id") Integer entityId,
            @Query("entity_type") String entityType,
            @Query("cuisines") String cuisinesArray,
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("start") Integer start
    );

    @GET("/api/v2.1/cities")
    Call<CitiesResult> cities(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("city_ids") String cityIds,
            @Query("count") Integer count);

    @GET("/api/v2.1/cuisines")
    Call<CuisinesResult> cuisines(
            @Query("city_id") Integer cityId
    );

}
