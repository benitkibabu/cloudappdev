package com.cloudappdev.ben.virtualkitchen.rest;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Benit Kibabu on 30/05/2017.
 */

public class IngredientService {

    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private boolean added, removed;


    public static IngredientService getInstance(Context context){
        IngredientService ingredientService =  new IngredientService();
        ingredientService.setContext(context);
        return ingredientService;
    }

    public boolean addIngredient(final Ingredient ingredient){
        added = false;
        APIService service = AppConfig.getAPIService();
        service.addIngredient(AppController.getInstance().appKey(), ingredient)
        .enqueue(new Callback<Ingredient>() {
            @Override
            public void onResponse(@NonNull Call<Ingredient> call, @NonNull Response<Ingredient> response) {
                if(response.isSuccessful()){
                    added = true;
                }
            }
            @Override
            public void onFailure(@NonNull Call<Ingredient> call, @NonNull Throwable t) {
                Log.e("NotSuccessful", t.toString());
            }
        });

        return added;
    }

    public boolean removeIngredient(final Ingredient ingredient) {
        removed = false;
        APIService service = AppConfig.getAPIService();
        service.removeIngredient(ingredient.getId(), AppController.getInstance().appKey())
                .enqueue(new Callback<Ingredient>() {
                    @Override
                    public void onResponse(@NonNull Call<Ingredient> call, @NonNull Response<Ingredient> response) {
                        if(response.isSuccessful()){
                            removed = true;
                        }else{
                            Log.e("NotSuccessful", response.toString());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Ingredient> call, @NonNull Throwable t) {
                        Log.e("NotSuccessful", t.toString());
                    }
                });

        return removed;
    }

    private List<Ingredient> ingredientList;
    public List<Ingredient> retrieveIngredients(int userID){
        ingredientList = new ArrayList<>();
        APIService service = AppConfig.getAPIService();
        service.fetchMyIngredient(AppController.getInstance().appKey(), userID)
                .enqueue(new Callback<List<Ingredient>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Ingredient>> call, @NonNull Response<List<Ingredient>> response) {
                        if(response.isSuccessful()){
                            ingredientList = response.body();
                            Log.d("Result", ingredientList.size()+"");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Ingredient>> call, @NonNull Throwable t) {
                        Log.e("GetMyIngredients", t.toString());
                    }
                });

        return ingredientList;
    }

}
