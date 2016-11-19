package com.cloudappdev.ben.virtualkitchen.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.cloudappdev.ben.virtualkitchen.MainActivity;
import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.adapter.ContextMenuRecyclerView;
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

public class RecipesActivity extends AppCompatActivity {

    User data;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecipeRecycleViewAdapter adapter;
    String API_ID;
    String API_KEY;

    List<Recipe> recipeList;
    int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        API_ID = getString(R.string.edamam_api_id);
        API_KEY = getString(R.string.edamam_api_key);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView  = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        registerForContextMenu(recyclerView);

        adapter = new RecipeRecycleViewAdapter(this, R.layout.recipe_item);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecipeRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = new Intent(RecipesActivity.this, RecipeDetails.class);
                Recipe r = recipeList.get(position);
                i.putExtra("Recipe", r);
                startActivity(i);
            }
        });

        adapter.setOnItemLongClickListener(new RecipeRecycleViewAdapter.OnItemLongCLickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                itemPosition = position;
                Snackbar.make(view, "WOuld you like to delete this? " + itemPosition,
                        Snackbar.LENGTH_LONG)
                        .setAction("Delete", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }
        });

    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_context, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        ContextMenuRecyclerView.RecyclerContextMenuInfo info =
//                (ContextMenuRecyclerView.RecyclerContextMenuInfo) item.getMenuInfo();
//
//        return false;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipes, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        //searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AppController.getInstance().searchKey = query;
                getRecipe(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == android.R.id.home){
            goBack();
        }

        return super.onOptionsItemSelected(item);
    }

    void loadUser(){
        if(AppController.getInstance().getUser() != null) {
            data = AppController.getInstance().getUser();
        }else{
            goBack();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        loadUser();
        if(AppController.getInstance().getNavFragement() != null && !AppController.getInstance().getNavFragement().isEmpty()){
            String f= AppController.getInstance().getNavFragement();
            if(f.equals("R")){
                getRecipe(AppController.getInstance().searchKey);
            }else{
                getMyFavourite();
            }
        }else{
            goBack();
        }
    }

    public void getRecipe(final String query){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Data...");

        final String TAG = "Get Recipe";

        dialog.show();

        Uri url = Uri.parse( AppConfig.RECIPE_API)
                .buildUpon()
                .appendQueryParameter("q", query)
                .appendQueryParameter("app_id", API_ID)
                .appendQueryParameter("app_key", API_KEY)
                .build();

        StringRequest request = new StringRequest(Request.Method.GET,url.toString(), new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                recipeList = new ArrayList<>();
                setTitle("Online Recipes");
                Log.d("API RESULT", response);
                try{
                    JSONObject obj = new JSONObject(response);
                    JSONArray hits = obj.getJSONArray("hits");
                    for(int i = 0; i<hits.length(); i++) {
                        JSONObject r = hits.getJSONObject(i);
                        JSONObject re = r.getJSONObject("recipe");
                        JSONArray dL = re.getJSONArray("dietLabels");
                        JSONArray hL = re.getJSONArray("healthLabels");
                        JSONArray ca = re.getJSONArray("cautions");
                        JSONArray iL = re.getJSONArray("ingredientLines");
                        JSONArray ing = re.getJSONArray("ingredients");

                        ArrayList<Ingredient> ingredientsList = new ArrayList<>();
                        String dietLabels = " ";
                        String healthLabels = " ";
                        String cautions = " ";
                        String ingredientLines = " ";

                        for(int x = 0; x < dL.length(); x++){
                            dietLabels += dL.getString(x)+", ";
                        }
                        for(int x = 0; x < hL.length(); x++){
                            healthLabels += hL.getString(x)+", ";
                        }
                        for(int x = 0; x < ca.length(); x++){
                            cautions += ca.getString(x)+", ";
                        }
                        for(int x = 0; x < iL.length(); x++){
                            ingredientLines += iL.getString(x)+", ";
                        }
                        for(int x = 0; x < ing.length(); x++){
                            JSONObject val = ing.getJSONObject(x);
                            Ingredient ingr = new Ingredient(val.getString("text"),
                                    val.getDouble("quantity"),val.getString("measure"),
                                    val.getDouble("weight"), val.getString("food"));
                            ingredientsList.add(ingr);
                        }


                        Recipe rec = new Recipe(re.getString("uri"), re.getString("label"), re.getString("image"),
                                re.getString("source"), re.getString("url"),re.getString("shareAs"),
                                Math.floor(re.getDouble("yield")), dietLabels, healthLabels, cautions, ingredientLines,
                                Math.floor(re.getDouble("calories")),  Math.floor(re.getDouble("totalWeight")));
                        recipeList.add(rec);
                    }
                    adapter.addAll(recipeList);

                } catch (JSONException e) {
                    Log.d("API REQUEST catch:", e.getMessage());
                }

                dialog.dismiss();

            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
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
        });

        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    private void getMyFavourite(){
        String TAG = "Get MyFavourite";
        Uri url = Uri.parse(AppConfig.INTERNAL_RECIPES_API)
                .buildUpon()
                .appendQueryParameter("userid", String.valueOf(data.getId()))
                .build();

        Log.w("Url", url.toString());

        JsonArrayRequest request = new JsonArrayRequest(url.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.w("MyF", response.toString());
                        setTitle("My Favourites");
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

    void goBack(){
        Intent upIntent = new Intent(this, MainActivity.class);
        upIntent.putExtra("User", data);
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
}
