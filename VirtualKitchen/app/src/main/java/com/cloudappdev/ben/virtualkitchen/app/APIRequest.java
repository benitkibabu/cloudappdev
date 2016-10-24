package com.cloudappdev.ben.virtualkitchen.app;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cloudappdev.ben.virtualkitchen.models.Food;
import com.cloudappdev.ben.virtualkitchen.models.Ingredient;
import com.cloudappdev.ben.virtualkitchen.models.Measure;
import com.cloudappdev.ben.virtualkitchen.models.NutrientInfo;
import com.cloudappdev.ben.virtualkitchen.models.Recipe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben on 24/10/2016.
 */

public class APIRequest {
    public static Ingredient getIngredient(final String query, final String API_KEY, final String API_ID){
        return null;
    }
    public static Recipe getRecipe(final String query, final String API_KEY, final String API_ID){
        Recipe recipe = null;
        Ingredient i = null;
        Food food = null;
        Measure measure = null;
        NutrientInfo info = null;
        final String TAG = "Get Recipe";

        StringRequest request = new StringRequest(Request.Method.GET, AppConfig.RECIPE_API, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("Login Activity", response);
                try{
                    JSONObject obj = new JSONObject(response);
                    JSONObject hits = obj.getJSONObject("hits");
                    JSONObject recipe = obj.getJSONObject("recipe");

                } catch (JSONException e) {
                    Log.d("Login Activity", e.getMessage());
                }

            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                }

                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                }else if(error instanceof NoConnectionError){
                    Log.e("Volley", "NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "ParseError");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> values = new HashMap<>();
                values.put("app_key", API_KEY);
                values.put("app_id", API_ID);
                values.put("q", query);
                return values;
            };
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request, TAG);
        return recipe;
    }
}
