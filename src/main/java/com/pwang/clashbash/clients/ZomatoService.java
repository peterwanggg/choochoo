package com.pwang.clashbash.clients;

import com.pwang.clashbash.objects.api.zomato.SearchResult;
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

}
