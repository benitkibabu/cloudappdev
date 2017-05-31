package com.cloudappdev.ben.virtualkitchen.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.helper.AppPreference;
import com.cloudappdev.ben.virtualkitchen.helper.SQLiteHandler;
import com.cloudappdev.ben.virtualkitchen.rest.APIService;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.MyRecipes;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetails extends AppCompatActivity {

    Button instructionBtn, findBtn;
    private ProgressDialog mProgressDialog;
    TextView title ;
    TextView ingredients;
    TextView nutrition ;
    TextView summary;
    ImageView img, fav_icon;
    FloatingActionButton fab;

    List<MyRecipes> recipeList;
    User user;
    MyRecipes r;

    APIService service;
    AppPreference pref;
    SQLiteHandler db;

    boolean isMyFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_recipe_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new SQLiteHandler(this);
        pref = new AppPreference(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        title = (TextView) findViewById(R.id.title);
        ingredients = (TextView) findViewById(R.id.ingredient);
        nutrition = (TextView) findViewById(R.id.nutrition);
        summary = (TextView) findViewById(R.id.summary);
        instructionBtn = (Button) findViewById(R.id.instruction_btn);
        fav_icon = (ImageView) findViewById(R.id.fav_icon);
        img = (ImageView) findViewById(R.id.recipe_img);
        findBtn = (Button) findViewById(R.id.view_ingredient);

        final String f = AppController.getInstance().getNavFragment();

        if(getIntent().hasExtra("Recipe") && db != null){
            user = db.getUser();
            r = (MyRecipes) getIntent().getSerializableExtra("Recipe");

            getMyFavourite(title);

            if(f.equals("R") && !isMyFavorite){
                fav_icon.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
            }else{
                fab.setVisibility(View.INVISIBLE);
                fav_icon.setVisibility(View.VISIBLE);
            }

            setTitle(r.getLabel());

            instructionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(RecipeDetails.this, WebViewActivity.class);
                    i.putExtra("URL", r.getUrl());
                    i.putExtra("R", r);
                    startActivity(i);
                }
            });

            findBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Coming Soon!", Snackbar.LENGTH_LONG).show();
                }
            });

            title.setText(r.getLabel());

            String nut = "";
            String sum = "";
            int count = r.getIngredientCount();

            String i = count+ " Ingredients: \n" + r.getIngredientLines();
            nut += "Diet Labels: \n" +r.getDietLabels();
            nut += "\n\nHealth Labels: \n" +r.getHealthLabels();
            nut += "\n\nCalories: \n" + Math.floor(r.getCalories()) + "/ Serving";
            nut += "\n\nTotal Weight: " + Math.floor(r.getTotalWeight());
            nut += "\n\nCautions: \n" +r.getCautions();
            sum += "Source: " + r.getSource();

            ingredients.setText(i);
            nutrition.setText(nut);
            summary.setText(sum);

            Picasso.with(getApplicationContext())
                    .load(r.getImage())
                    .placeholder(R.drawable.progress_animation)
                    .resize(512,512).centerCrop()
                    .into(img);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    r.setUser_id(user.getId());
                    saveRecipe(r, view);
                }
            });
        }else{
            goBack();
        }
    }

    private void getMyFavourite(final View v) {
        showProgressDialog("Fetching Favorites...");
        service = AppConfig.getAPIService();
        service.fetchMyRecipes(AppController.getInstance().appKey(), user.getId())
                .enqueue(new Callback<List<MyRecipes>>() {
                    @Override
                    public void onResponse(Call<List<MyRecipes>> call, Response<List<MyRecipes>> response) {
                        hideProgressDialog();
                        if(response.isSuccessful()){
                            recipeList = response.body();
                            for(MyRecipes rs : recipeList){
                                if(rs.getLabel().equals(r.getLabel())){
                                    isMyFavorite = true;
                                    fab.setVisibility(View.INVISIBLE);
                                    fav_icon.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MyRecipes>> call, Throwable t) {
                        hideProgressDialog();
                        Log.e ("GetFavRecipe", t.toString());
                    }
                });
    }

    void saveRecipe(final MyRecipes recipe, final View view) {
        showProgressDialog("Saving!...");
        service = AppConfig.getAPIService();
        service.addRecipeToFavorite(AppController.getInstance().appKey(), recipe)
                .enqueue(new Callback<MyRecipes>() {
                    @Override
                    public void onResponse(Call<MyRecipes> call, Response<MyRecipes> response) {
                        hideProgressDialog();
                        if(response.isSuccessful()){
                            Snackbar.make(title, "Recipe Added to Favorites!",
                                    Snackbar.LENGTH_LONG).show();
                            fab.setVisibility(View.INVISIBLE);
                            fav_icon.setVisibility(View.VISIBLE);
                        }else{
                            Snackbar.make(title, "Failed to Add to Favorites!",
                                    Snackbar.LENGTH_LONG).show();
                            Log.e("SAVEFAIL", response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyRecipes> call, Throwable t) {
                        hideProgressDialog();
                        Log.e("SAVEFAILERR", t.toString());
                    }
                });
    }

    void removeRecipe(final MyRecipes recipe, final View view) {
        showProgressDialog("Removing!...");
        service = AppConfig.getAPIService();
        service.removeRecipe(recipe.getId(), AppController.getInstance().appKey())
                .enqueue(new Callback<MyRecipes>() {
                    @Override
                    public void onResponse(Call<MyRecipes> call, Response<MyRecipes> response) {
                        if(response.isSuccessful()){
                            isMyFavorite = false;
                            fab.setVisibility(View.VISIBLE);
                            fav_icon.setVisibility(View.INVISIBLE);
                            Snackbar.make(view, "Item has been removed!",
                                    Snackbar.LENGTH_LONG).show();
                        }else{
                            Snackbar.make(view, "Failed to Remove Item",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyRecipes> call, Throwable t) {
                        Log.e("RemoveRecipe", t.toString());
                    }
                });
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
    public void onResume(){
        super.onResume();
        if(!AppConfig.isNetworkAvailable(this)){
            Snackbar.make(title, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Connect", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Connect method goes here
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    }).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!AppController.getInstance().getNavFragment().equals("R") || isMyFavorite) {
            getMenuInflater().inflate(R.menu.menu_recipe_deails, menu);
        }
        return true;
    }

    void goBack(){
        Intent upIntent = new Intent(this, RecipesActivity.class);
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
        if (id == R.id.action_remove) {
            r.setUser_id(user.getId());

            removeRecipe(r, title);
            return true;
        }
        else if(id == android.R.id.home){
            //goBack();//
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
