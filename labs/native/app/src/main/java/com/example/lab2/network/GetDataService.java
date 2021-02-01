package com.example.lab2.network;

import com.example.lab2.model.Flower;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("flowersApi/getAll")
    Call<List<Flower>> getAll();

    @GET("flowersApi/getAllNames")
    Call<List<String>> getAllNames();

    @POST("flowersApi/save")
    Call<Long> addFlower(@Body Flower flower);

    @PUT("flowersApi/update")
    Call<Flower> updateFlower(@Body Flower flower);

    @DELETE("flowersApi/delete/{id}")
    Call<Boolean> deleteFlower(@Path("id") Long id);
}