package com.cloudappdev.ben.virtualkitchen.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyIngredients extends AppCompatActivity {

    User user;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecipeRecycleViewAdapter adapter;
    private ProgressDialog mProgressDialog;

    List<Ingredient> myIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ingredients);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(AppController.getInstance().getUser() != null){
            user = AppController.getInstance().getUser();
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

    private void getMyIngredient(){
        final String TAG = "Get Ingredients";
        Uri url = Uri.parse(AppConfig.INTERNAL_INGREDIENT_API)
                .buildUpon()
                .appendQueryParameter("userid", String.valueOf(user.getId()))
                .build();

        Log.w("Url", url.toString());

        JsonArrayRequest request = new JsonArrayRequest(url.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.w(TAG, response.toString());
                        myIngredients = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject j = response.getJSONObject(i);
                                if(j.getInt("userid") == user.getId()) {
                                    Ingredient in = new Ingredient(j.getInt("id"),
                                            j.getString("uri"), j.getDouble("quantity"),
                                            j.getString("measure"), j.getDouble("weight"),
                                            j.getString("food"), j.getInt("userid"));

                                    myIngredients.add(in);
                                }
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

    void addIngredient(final Ingredient ingred, final View view) throws JSONException {
        showProgressDialog();
        final String TAG = "Saving Recipe";

        JSONObject jobj = new JSONObject();

        jobj.put("uri", ingred.getUri());
        jobj.put("quantity", ingred.getQuantity());
        jobj.put("measure", ingred.getMeasure());
        jobj.put("weight", ingred.getWeight());
        jobj.put("food", ingred.getFood());
        jobj.put("userid", user.getId());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
                AppConfig.INTERNAL_RECIPES_API,
                jobj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Snackbar.make(view, "Recipe added to favourite ", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        hideProgressDialog();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(objectRequest, TAG);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.saving_recipe));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }else{
            mProgressDialog.setMessage(getString(R.string.saving_recipe));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
