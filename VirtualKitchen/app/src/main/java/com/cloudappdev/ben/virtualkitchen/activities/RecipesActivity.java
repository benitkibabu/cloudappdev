package com.cloudappdev.ben.virtualkitchen.activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

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
import com.cloudappdev.ben.virtualkitchen.main.MainActivity;
import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.adapter.CustomRecycleViewAdapter;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.Ingredient;
import com.cloudappdev.ben.virtualkitchen.models.Recipe;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipesActivity extends AppCompatActivity {

    User user;
    ProgressDialog mProgressDialog;

    private RecyclerView recyclerView;
    private CustomRecycleViewAdapter adapter;
    ProgressBar progressBar;
    String API_ID;
    String API_KEY;

    List<Recipe> recipeList;
    int itemPosition;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_recipes);

        API_ID = getString(R.string.edamam_api_id);
        API_KEY = getString(R.string.edamam_api_key);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        registerForContextMenu(recyclerView);

        adapter = new CustomRecycleViewAdapter(this, R.layout.recipe_item);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CustomRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i = new Intent(RecipesActivity.this, RecipeDetails.class);
                Recipe r = recipeList.get(position);
                i.putExtra("Recipe", r);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(RecipesActivity.this).toBundle());
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //If we are viewing the Online Recipes, We should be able to search it.
        if (AppController.getInstance().getNavFragement().equals("R")) {
            getMenuInflater().inflate(R.menu.menu_recipes, menu);

            // Associate searchable configuration with the SearchView
            // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            //searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()));
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

        return true;
    }



    void loadUser() {
        if (AppController.getInstance().getUser() != null) {
            user = AppController.getInstance().getUser();
        } else {
            goBack();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUser();
        if (AppConfig.isNetworkAvailable(this)) {
            if (AppController.getInstance().getNavFragement() != null && !AppController.getInstance().getNavFragement().isEmpty()) {
                String f = AppController.getInstance().getNavFragement();
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
                            //Connect method goes here
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    }).show();
        }

    }

    //Search Recipes online from external API
    public void getRecipe(final String query) {

        showProgressDialog();

        final String TAG = "Get Recipe";

        Uri url = Uri.parse(AppConfig.RECIPE_API)
                .buildUpon()
                .appendQueryParameter("q", query)
                .appendQueryParameter("app_id", API_ID)
                .appendQueryParameter("app_key", API_KEY)
                .build();

        //Log.w("RecipeURL", url.toString());

        StringRequest request = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recipeList = new ArrayList<>();
                setTitle("Online Recipes");
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray hits = obj.getJSONArray("hits");
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject r = hits.getJSONObject(i);
                        JSONObject re = r.getJSONObject("recipe");
                        JSONArray dL = re.getJSONArray("dietLabels");
                        JSONArray hL = re.getJSONArray("healthLabels");
                        JSONArray ca = re.getJSONArray("cautions");
                        JSONArray iL = re.getJSONArray("ingredientLines");
                        JSONArray ing = re.getJSONArray("ingredients");

                        ArrayList<Ingredient> ingredientsList = new ArrayList<>();
                        String dietLabels = "";
                        String healthLabels = "";
                        String cautions = "";
                        String ingredientLines = "";

                        for (int x = 0; x < dL.length(); x++) {
                            dietLabels += dL.getString(x) + "\n";
                        }
                        for (int x = 0; x < hL.length(); x++) {
                            healthLabels += hL.getString(x) + "\n";
                        }
                        for (int x = 0; x < ca.length(); x++) {
                            cautions += ca.getString(x) + "\n";
                        }
                        for (int x = 0; x < iL.length(); x++) {
                            ingredientLines += iL.getString(x) + "\n";
                        }
                        for (int x = 0; x < ing.length(); x++) {
                            JSONObject val = ing.getJSONObject(x);
//                            Ingredient ingr = new Ingredient(val.getString("text"),
//                                    val.getDouble("weights"), user.getId());
//                            ingredientsList.add(ingr);
                        }

                        Log.w("Lines", ingredientLines);

                        Recipe rec = new Recipe(re.getString("uri"), re.getString("label"), re.getString("image"),
                                re.getString("source"), re.getString("url"), re.getString("shareAs"),
                                Math.floor(re.getDouble("yield")), dietLabels, healthLabels, cautions, ingredientLines,
                                Math.floor(re.getDouble("calories")), Math.floor(re.getDouble("totalWeight")));
                        rec.setIngredients(ingredientsList);
                        recipeList.add(rec);
                    }
                    adapter.addAll(recipeList);
                    if (recipeList.isEmpty()) {
                        Snackbar.make(recyclerView, "Recipes were not found!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Done", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    }

                } catch (JSONException e) {
                    Log.d("#catch:", e.getMessage());
                }

                hideProgressDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);
                }

                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                } else if (error instanceof NoConnectionError) {
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                headers.put("app_key", AppController.getInstance().appKey());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    //Retrieve User favourite Recipe from internal API
    private void getMyFavourite() {
        showProgressDialog();
        String TAG = "Get MyFavourite";
        Uri url = Uri.parse(AppConfig.INTERNAL_RECIPES_API)
                .buildUpon()
                //.appendQueryParameter("consumer_id", String.valueOf(user.getId()))
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
                                if(j.getInt("consumer_id") == user.getId()) {
                                    Recipe r = new Recipe(
                                            j.getInt("id"),
                                            j.getString("uri"), j.getString("label"),
                                            j.getString("imageurl"), j.getString("source"),
                                            j.getString("url"), j.getString("shareas"),
                                            j.getDouble("yield"), j.getString("dietlabel"),
                                            j.getString("healthlabel"), j.getString("caution"),
                                            j.getString("ingredientlines"), j.getDouble("calories"),
                                            j.getDouble("totalweight"),
                                            j.getInt("consumer_id"));

                                    recipeList.add(r);
                                }
                            }

                            adapter.addAll(recipeList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hideProgressDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                headers.put("app_key", AppController.getInstance().appKey());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(request, TAG);
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
            //goBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressDialog() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Recipes Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
