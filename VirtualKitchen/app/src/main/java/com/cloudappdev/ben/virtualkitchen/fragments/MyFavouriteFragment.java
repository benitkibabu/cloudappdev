package com.cloudappdev.ben.virtualkitchen.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.adapter.RecipeRecycleViewAdapter;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.Recipe;
import com.cloudappdev.ben.virtualkitchen.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *created by Ben 23/10/2016
 *
 */
public class MyFavouriteFragment extends Fragment {
    User user;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecipeRecycleViewAdapter adapter;

    List<Recipe> recipeList;

    public MyFavouriteFragment() {
        // Required empty public constructor
    }

    public static MyFavouriteFragment newInstance() {
        MyFavouriteFragment fragment = new MyFavouriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppController.getUser() != null){
            user = AppController.getUser();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favourite, container, false);



        recyclerView  = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        adapter = new RecipeRecycleViewAdapter(getActivity(), R.layout.recipe_item);

        recyclerView.setAdapter(adapter);

        getMyFavourite();



        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getMyFavourite();
    }

    private void getMyFavourite(){
        String TAG = "Get MyFavourite";
        Uri url = Uri.parse(AppConfig.INTERNAL_RECIPES_API)
                .buildUpon()
                .appendQueryParameter("userid", String.valueOf(user.getId()))
                .build();

        Log.w("Url", url.toString());

        JsonArrayRequest request = new JsonArrayRequest(url.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.w("MyF", response.toString());
                        recipeList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject j = response.getJSONObject(i);
                                Recipe r = new Recipe(j.getInt("id"),
                                        j.getString("uri"), j.getString("label"),
                                        j.getString("imageurl"), j.getString("source"),
                                        j.getString("url"), j.getString("shareas"),
                                        j.getDouble("yield"), j.getString("dietlabel"),
                                        j.getString("healthlabel"), j.getString("caution"),
                                        j.getString("ingredientlines"), j.getDouble("calories"),
                                        j.getDouble("totalweight"));

                                recipeList.add(r);
                            }

                            adapter.addAll(recipeList);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        AppController.getInstance().addToRequestQueue(request, TAG);
    }

}
