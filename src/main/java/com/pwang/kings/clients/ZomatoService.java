package com.pwang.kings.clients;

import com.pwang.kings.objects.api.zomato.CitiesResult;
import com.pwang.kings.objects.api.zomato.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * @author pwang on 12/26/17.
 */

public interface ZomatoService {

    @GET("/api/v2.1/search")
    Call<SearchResult> search(
            @Query("lat") double lat,
            @Query("lon") double lon);

    @GET("/api/v2.1/cities")
    Call<CitiesResult> cities(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("count") int count);

}
