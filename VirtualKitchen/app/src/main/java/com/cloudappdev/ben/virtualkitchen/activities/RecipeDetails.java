package com.cloudappdev.ben.virtualkitchen.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.Recipe;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RecipeDetails extends AppCompatActivity {


    User data;
    Recipe r;
    Button instructionBtn;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("Recipe") && AppController.getInstance().getUser() != null){
            data = AppController.getInstance().getUser();
            r = (Recipe) getIntent().getSerializableExtra("Recipe");
            setTitle(r.getLabel());

            TextView title = (TextView) findViewById(R.id.title);
            TextView ingredients = (TextView) findViewById(R.id.ingredient);
            TextView nutrition = (TextView) findViewById(R.id.nutrition);
            TextView summary = (TextView) findViewById(R.id.summary);

            instructionBtn = (Button) findViewById(R.id.instruction_btn);
            instructionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(RecipeDetails.this, WebViewActivity.class);
                    i.putExtra("URL", r.getUrl());
                    i.putExtra("R", r);
                    startActivity(i);
                }
            });

            title.setText(r.getLabel());

            String nut = "";
            String il = "";
            String hl = "";
            String sum = "";
            //int count = r.getIngredients().size();

            String i = "Ingredients: \n" + r.getIngredientLines();
            nut += r.getHealthLabels();
            nut += "\n\nCalories: " + Math.floor(r.getCalories()) + "/ Serving";
            sum += r.getSource();

            ingredients.setText(i);
            nutrition.setText(nut);
            summary.setText(sum);

            ImageView img = (ImageView) findViewById(R.id.recipe_img);
            Picasso.with(getApplicationContext())
                    .load(r.getImageUrl())
                    .resize(512,512).centerCrop()
                    .into(img);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String f= AppController.getInstance().getNavFragement();
                        if(f.equals("R")) {
                            saveRecipe(r, view);
                        }else{
                            Snackbar.make(view, "Already in your favourites",
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Dismiss", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            goBack();
        }
    }

    void saveRecipe(final Recipe recipe, final View view) throws JSONException {
        showProgressDialog();
        final String TAG = "Saving Recipe";

        JSONObject jobj = new JSONObject();

        jobj.put("uri", recipe.getUri());
        jobj.put("label",recipe.getLabel());
        jobj.put("imageurl", recipe.getImageUrl());
        jobj.put("source", recipe.getSource());
        jobj.put("url", recipe.getUrl());
        jobj.put("shareas", recipe.getShareAs());
        jobj.put("dietlabel", recipe.getDietLabels());
        jobj.put("healthlabel", recipe.getHealthLabels());
        jobj.put("caution", recipe.getCautions());
        jobj.put("ingredientlines", recipe.getIngredientLines());
        jobj.put("yield", recipe.getYield());
        jobj.put("calories", recipe.getCalories());
        jobj.put("totalweight", recipe.getTotalWeight());
        jobj.put("userid", data.getId());

        Log.i("Param", jobj.toString());

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

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipes, menu);
        return true;
    }

    void goBack(){
        Intent upIntent = new Intent(this, RecipesActivity.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == android.R.id.home){
            goBack();//NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
