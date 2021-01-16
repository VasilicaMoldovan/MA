package com.example.tryexam.network;

import com.example.tryexam.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetDataService {
    @GET("/types")
    Call<List<String>> getAll();

    @GET("/recipes/{type}")
    Call<List<Recipe>> getAllRecipes(@Path("type") String type);

    @POST("/recipe")
    Call<Recipe> addRecipe(@Body Recipe recipe);

    @DELETE("/recipe/{id}")
    Call<Recipe> deleteFlower(@Path("id") int id);

    @GET("/low")
    Call<List<Recipe>> getRecipesAscending();

    @POST("/increment")
    Call<Recipe> increment(@Body Recipe recipe);

}
