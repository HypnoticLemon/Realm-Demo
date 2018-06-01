package com.example.vikrant.demoapplicationjava;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Vikrant on 12-01-2018.
 */

public interface ApiInterface {

    @GET("posts")
    Call<List<DataModel>> getModelData();
}
