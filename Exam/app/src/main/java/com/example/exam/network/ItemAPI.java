package com.example.exam.network;

import com.example.exam.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ItemAPI {

    @GET("/rules")
    Call<List<Item>> getAll();

    @GET("/rule/{id}")
    Call<Item> getRule(@Path("id") int id);

    @POST("/rule")
    Call<Item> add(@Body Item item);

    @POST("/process")
    Call<Item> update(@Body Item item);

    @GET("/order/{id}")
    Call<Item> getItem(@Path("id") int id);

}