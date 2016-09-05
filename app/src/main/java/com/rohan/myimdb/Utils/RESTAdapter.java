package com.rohan.myimdb.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rohan on 17-Jul-16.
 */
public class RESTAdapter {

    MoviesAPI moviesAPI;

    public RESTAdapter(String baseURL) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        moviesAPI = retrofit.create(MoviesAPI.class);
    }

    public MoviesAPI getMoviesAPI() {
        return moviesAPI;
    }
}
