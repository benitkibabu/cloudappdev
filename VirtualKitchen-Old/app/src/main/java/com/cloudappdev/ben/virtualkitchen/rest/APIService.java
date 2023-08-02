package com.cloudappdev.ben.virtualkitchen.rest;

import com.cloudappdev.ben.virtualkitchen.models.Ingredient;
import com.cloudappdev.ben.virtualkitchen.models.MyRecipes;
import com.cloudappdev.ben.virtualkitchen.models.RecipeResponse;
import com.cloudappdev.ben.virtualkitchen.models.UPCResponse;
import com.cloudappdev.ben.virtualkitchen.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ben on 24/10/2016.
 */

public interface APIService {

    //All App User Request
    @GET("app_users/")
    Call<List<User>> getAllUser(@Query("app_key") String appKey);

    @GET("app_users/login")
    Call<User> login(@Query("app_key") String appKey,  @Query("username") String username,
                      @Query("encrypted_password") String encrypted_password);

    //Register a new user
    @POST("app_users/")
    Call<User> registerUser(@Query("app_key") String appKey, @Body User user);

    //Update User info
    @PUT("app_users/{id}")
    Call<User> updateUser(@Path("id") int id, @Query("app_key") String appKey, @Body User user);


    /*
     Service for Ingredients
    */
    @GET("ingredients/")
    Call<List<Ingredient>> fetchMyIngredient(@Query("app_key") String appKey,
                                       @Query("app_user_id") int userID);

    @POST("ingredients/")
    Call<Ingredient> addIngredient(@Query("app_key") String appKey, @Body Ingredient ingredient);

    @DELETE("ingredients/{id}")
    Call<Ingredient> removeIngredient(@Path("id") int id, @Query("app_key") String appKey);

    /*
    Service for Recipes
    */
    @GET("recipes/")
    Call<List<MyRecipes>> fetchMyRecipes(@Query("app_key") String appKey, @Query("app_user_id") int userID);

    @POST("recipes/")
    Call<MyRecipes> addRecipeToFavorite(@Query("app_key") String appKey, @Body MyRecipes recipe);

    @DELETE("recipes/{id}")
    Call<MyRecipes> removeRecipe( @Path("id") int id, @Query("app_key") String appKey);

    /*
        External API Calls
    */
    @GET("search")
    Call<RecipeResponse> fetchOnlineRecepies(@Query("app_key") String app_key,
                                             @Query("app_id") String app_id,
                                             @Query("q") String q);

    @GET("lookup")
    Call<UPCResponse> fetchUPCItem(@Query("upc") String upc);

}
