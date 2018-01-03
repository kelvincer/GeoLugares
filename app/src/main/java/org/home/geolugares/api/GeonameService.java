package org.home.geolugares.api;

import org.home.geolugares.response.GeonaneSearchResponse;
import org.home.geolugares.response.LugaresCercanosResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kelvin on 15/11/2017.
 */

public interface GeonameService {

    @GET("searchJSON")
    Call<GeonaneSearchResponse> buscarLugares(@Query("q") String name,
                                              @Query("country") List<String> countries,
                                              @Query("maxRows") String rows,
                                              @Query("lang") String lengua,
                                              @Query("username") String username);

    @GET("findNearbyJSON")
    Call<LugaresCercanosResponse> buscarLugaresCercanos(@Query("lat") String latitud,
                                                        @Query("lng") String longitud,
                                                        @Query("radius") String radio,
                                                        @Query("maxRows") String rows,
                                                        @Query("featureClass") String clase,
                                                        @Query("username") String username);

}
