package com.example.vikrant.demoapplicationjava;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vikrant on 12-01-2018.
 */

public class ApiClient {

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    //https://jsonplaceholder.typicode.com/posts
    private static Retrofit retrofit = null;

    public static Retrofit  getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
