package org.home.geolugares.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kelvin on 10/12/2017.
 */

public class RetrofitDirectionsClient {

    private Retrofit retrofit;
    private final static String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";

    public RetrofitDirectionsClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public DirectionsService getDirectionsService() {
        return retrofit.create(DirectionsService.class);
    }
}
