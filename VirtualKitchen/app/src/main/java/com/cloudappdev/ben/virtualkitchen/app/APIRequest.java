package com.cloudappdev.ben.virtualkitchen.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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
import com.cloudappdev.ben.virtualkitchen.models.Ingredient;
import com.cloudappdev.ben.virtualkitchen.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 24/10/2016.
 */

public class APIRequest {
    public static Ingredient getIngredient(final String query, final String API_KEY, final String API_ID, final Context context){
        return null;
    }

}
