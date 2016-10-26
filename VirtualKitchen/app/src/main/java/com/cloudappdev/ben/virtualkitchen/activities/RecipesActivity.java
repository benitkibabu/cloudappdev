package com.cloudappdev.ben.virtualkitchen.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cloudappdev.ben.virtualkitchen.MainActivity;
import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.adapter.CustomPageAdapter;
import com.cloudappdev.ben.virtualkitchen.fragments.MyFavouriteFragment;
import com.cloudappdev.ben.virtualkitchen.fragments.RecipeListFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    private CustomPageAdapter adapter;

    private ViewPager mViewPager;
    private List<Fragment> fragmentList;
    private List<String> titles;
    Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentList = new ArrayList<>();
        addFragment();

        adapter = new CustomPageAdapter(getSupportFragmentManager(), fragmentList, titles);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    void loadUser(){
        if(getIntent().hasExtra("User")) {
            if (!getIntent().getExtras().isEmpty()) {
                data = getIntent().getBundleExtra("User");
            }
        }else{
            goBack();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        loadUser();
    }

    void addFragment(){
        Fragment f;
        titles = new ArrayList<>();
        titles.add("Recipe");

        f = RecipeListFragment.newInstance("User", data);
        fragmentList.add(f);

        titles.add("My Favourite");
        f = MyFavouriteFragment.newInstance("User", data);
        fragmentList.add(f);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipes, menu);
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
            //NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
