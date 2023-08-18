package com.cloudappdev.ben.virtualkitchen.activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.cloudappdev.ben.virtualkitchen.helper.FavouriteSQLiteHandler;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.adapter.CustomRecycleViewAdapter;
import com.cloudappdev.ben.virtualkitchen.helper.AppPreference;
import com.cloudappdev.ben.virtualkitchen.helper.SQLiteHandler;
import com.cloudappdev.ben.virtualkitchen.rest.APIService;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.main.MainActivity;
import com.cloudappdev.ben.virtualkitchen.models.Hit;
import com.cloudappdev.ben.virtualkitchen.models.MyRecipes;
import com.cloudappdev.ben.virtualkitchen.models.Recipe;
import com.cloudappdev.ben.virtualkitchen.models.RecipeResponse;
import com.cloudappdev.ben.virtualkitchen.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity {

    User user;

    private RecyclerView recyclerView;
    private CustomRecycleViewAdapter adapter;
    private ProgressDialog mProgressDialog;
    String APP_ID;
    String APP_KEY;

    List<MyRecipes> recipeList;
    int itemPosition;

    APIService service;
    AppPreference pref;
    SQLiteHandler db;
    FavouriteSQLiteHandler fdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_recipes);

        APP_ID = getString(R.string.edamam_api_id);
        APP_KEY = getString(R.string.edamam_api_key);

        db = new SQLiteHandler(this);
        fdb = new FavouriteSQLiteHandler(this);
        pref = new AppPreference(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        registerForContextMenu(recyclerView);

        adapter = new CustomRecycleViewAdapter(this, R.layout.recipe_item);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(!recipeList.isEmpty()) {
                    Intent i = new Intent(RecipesActivity.this, RecipeDetails.class);
                    MyRecipes r = recipeList.get(position);
                    i.putExtra("Recipe", r);
                    startActivity(i, ActivityOptions.makeSceneTransitionAnimation(RecipesActivity.this).toBundle());
                }else{
                    Snackbar.make(view, "Please wait until Items are loaded",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });

        adapter.setOnItemLongClickListener(new CustomRecycleViewAdapter.OnItemLongCLickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                itemPosition = position;
                Snackbar.make(view, "Would you like to remove this? " + itemPosition,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setAction("No", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
            }
        });

    }

    //Search Recipes online from external API
    public void getRecipe(final String query) {
        showProgressDialog("Fetching Recipes!...");
        recipeList = new ArrayList<>();
        service = AppConfig.getAPIService(AppConfig.RECIPE_API);
        service.fetchOnlineRecepies(APP_KEY, APP_ID, query)
                .enqueue(new Callback<RecipeResponse>() {
                    @Override
                    public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                        hideProgressDialog();
                        if(response.isSuccessful()){
                            Log.i("RecipesResponse", response.body().getCount()+"");
                            List<Hit> hits = response.body().getHits();
                            for (Hit hit : hits){
                                if(hit != null) {
                                    if(hit.getRecipe() != null) {
                                        Recipe r = hit.getRecipe();

                                        MyRecipes recipes = new MyRecipes();
                                        recipes.setIngredientCount(r.getIngredientLines().size());
                                        recipes.setCalories(r.getCalories());
                                        recipes.setImage(r.getImage());
                                        recipes.setLabel(r.getLabel());
                                        recipes.setShareAs(r.getShareAs());
                                        recipes.setSource(r.getSource());
                                        recipes.setTotalWeight(r.getTotalWeight());
                                        recipes.setUri(r.getUri());
                                        recipes.setUrl(r.getUrl());
                                        recipes.setYield(r.getYield());
                                        recipes.setCautions(r.getCautions()+"");
                                        recipes.setHealthLabels(r.getHealthLabels()+"");
                                        recipes.setDietLabels(r.getDietLabels()+"");
                                        recipes.setIngredientLines(r.getIngredientLines()+"");

                                        recipeList.add(recipes);
                                    }
                                }
                            }

                            adapter.addAll(recipeList);
                        }else {
                            Snackbar.make(recyclerView, "Failed to retreive Recipes",
                                    Snackbar.LENGTH_LONG).show();
                            Log.i("GetOnlineRecipe", response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<RecipeResponse> call, Throwable t) {
                        hideProgressDialog();
                        Log.e("Err", t.toString());
                        Snackbar.make(recyclerView, "There was an error connecting to API",
                                Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    //Retrieve User favourite Recipe from internal API
    private void getMyFavourite() {
        showProgressDialog(getString(R.string.fetching_favourite_recipe));
        recipeList = new ArrayList<>();

        recipeList = fdb.getMyFavourites();
        if(recipeList == null || recipeList.size() == 0){
            hideProgressDialog();
            return;
        }

        adapter.addAll(recipeList);
        hideProgressDialog();
    }

    void goBack() {
        Intent upIntent = new Intent(this, MainActivity.class);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities(ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        } else {
            NavUtils.navigateUpTo(this, upIntent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog(String tag) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(tag);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }else{
            mProgressDialog.setMessage(tag);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (AppController.getInstance().getNavFragment().equals("R")) {
            getMenuInflater().inflate(R.menu.menu_recipes, menu);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            if (searchView != null) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        AppController.getInstance().searchKey = query;
                        if(query.equalsIgnoreCase("sick")){
                            AppController.getInstance().searchKey = "for sickness";
                        }
                        getRecipe(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            }
        }

        return true;
    }

    void loadUser() {
        if (db != null) {
            user = db.getUser();
        } else {
            goBack();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUser();
        if (AppConfig.isNetworkAvailable(this)) {
            if (AppController.getInstance().getNavFragment() != null && !AppController.getInstance().getNavFragment().isEmpty()) {
                String f = AppController.getInstance().getNavFragment();
                if (f.equals("R")) {
                    getRecipe(AppController.getInstance().searchKey);
                } else {
                    getMyFavourite();
                }
            } else {
                goBack();
            }
        } else {
            Snackbar.make(recyclerView, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Connect", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    }).show();
        }
    }
}
