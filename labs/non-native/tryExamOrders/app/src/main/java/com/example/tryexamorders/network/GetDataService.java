package com.example.tryexamorders.network;

import com.example.tryexamorders.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("/orders")
    Call<List<Order>> getAll();

    @GET("/order/{id}")
    Call<Order> getItem(@Path("id") int id);

    @GET("/recorded")
    Call<List<Order>> getRecorded();

    @GET("/my/{table}")
    Call<Order> getDetails(@Path("table") String table);

    @POST("/order")
    Call<Order> add(@Body Order order);

    @POST("/status")
    Call<Order> changeStatus(@Body Order order);

    @PUT("flowersApi/update")
    Call<Order> update(@Body Order order);

    @DELETE("flowersApi/delete/{id}")
    Call<Order> delete(@Path("id") int id);

}