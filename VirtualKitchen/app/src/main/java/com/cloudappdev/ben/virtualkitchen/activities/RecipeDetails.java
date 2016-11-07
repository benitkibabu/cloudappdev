package com.cloudappdev.ben.virtualkitchen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.Recipe;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.squareup.picasso.Picasso;

public class RecipeDetails extends AppCompatActivity {


    User data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("Recipe") && AppController.getUser() != null){
            data = AppController.getUser();
            Recipe r = (Recipe) getIntent().getSerializableExtra("Recipe");
            setTitle(r.getLabel());

            TextView title = (TextView) findViewById(R.id.title);
            TextView ingredients = (TextView) findViewById(R.id.ingredient);
            TextView nutrition = (TextView) findViewById(R.id.nutrition);
            TextView summary = (TextView) findViewById(R.id.summary);

            title.setText(r.getLabel());

            String nut = "";
            String il = "";
            String hl = "";
            String sum = "";
            int count = r.getIngredients().size();

            String i = count+" Ingredients: \n" + r.getIngredientLines();
            nut += r.getHealthLabels();
            nut += "\n\nCalories: " + Math.floor(r.getCaleries()) + "/ Serving";
            sum += r.getSource();

            ingredients.setText(i);
            nutrition.setText(nut);
            summary.setText(sum);

            ImageView img = (ImageView) findViewById(R.id.recipe_img);
            Picasso.with(getApplicationContext())
                    .load(r.getImageUrl())
                    .resize(512,512).centerCrop()
                    .into(img);
        }else{
            goBack();
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
