package com.cloudappdev.ben.virtualkitchen.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cloudappdev.ben.virtualkitchen.MainActivity;
import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.adapter.RecipeRecycleViewAdapter;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.Ingredient;
import com.cloudappdev.ben.virtualkitchen.models.Recipe;
import com.cloudappdev.ben.virtualkitchen.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyIngredients extends AppCompatActivity {

    User user;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecipeRecycleViewAdapter adapter;

    List<Ingredient> myIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ingredients);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(AppController.getUser() != null){
            user = AppController.getUser();
        }else{
            goBack();
        }

        recyclerView  = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_myingredients, menu);
        return true;
    }

    void goBack(){
        Intent upIntent = new Intent(this, MainActivity.class);
        if(NavUtils.shouldUpRecreateTask(this, upIntent)){
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
            finish();
        }else {
            NavUtils.navigateUpTo(this, upIntent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            return true;
        }
        else if (id == R.id.action_stores) {
            Intent i = new Intent(MyIngredients.this, MapsActivity.class);
            startActivity(i);
            return true;
        }
        else if(id == android.R.id.home){
            goBack();
        }
        return super.onOptionsItemSelected(item);
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
                        Log.w("My Ingredi", response.toString());
                        myIngredients = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject j = response.getJSONObject(i);
                                Ingredient in = new Ingredient(j.getInt("id"),
                                        j.getString("uri"), j.getDouble("quantity"),
                                        j.getString("measure"), j.getDouble("weight"),
                                        j.getString("food"));

                                myIngredients.add(in);
                            }
                            //adapter.addAll(myIngredients);
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
