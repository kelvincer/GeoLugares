package org.home.geolugares.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kelvin on 15/11/2017.
 */

public class RetrofitClient {

    private Retrofit retrofit;
    private final static String BASE_URL = "http://api.geonames.org/";

    public RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public GeonameService getGeonameService(){
        return retrofit.create(GeonameService.class);
    }
}
