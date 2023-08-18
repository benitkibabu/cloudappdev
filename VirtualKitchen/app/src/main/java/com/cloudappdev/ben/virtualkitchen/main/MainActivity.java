package com.cloudappdev.ben.virtualkitchen.main;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.activities.MyIngredients;
import com.cloudappdev.ben.virtualkitchen.activities.RecipesActivity;
import com.cloudappdev.ben.virtualkitchen.adapter.CustomMoodAdapter;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.helper.AppPreference;
import com.cloudappdev.ben.virtualkitchen.helper.SQLiteHandler;
import com.cloudappdev.ben.virtualkitchen.models.Emotes;
import com.cloudappdev.ben.virtualkitchen.models.User;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button recipeBtn, signoutBtn, ingredientBtn, favouriteBtn, moodBtn;
    TextView nameTv;
    CircleImageView profileImage;

    private final String TAG = this.getClass().getName();
    public String imageUrl;
    boolean fbLogout = false;
    User user;
    AppPreference pref;
    SQLiteHandler db;

    FrameLayout newMoodBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_main);

        db = new SQLiteHandler(this);
        pref = new AppPreference(this);

        if(!pref.getBooleanVal(pref.isLoggedIn)){
            logoutUser();
        }

        profileImage = (CircleImageView) findViewById(R.id.profile_img);
        newMoodBtn = (FrameLayout) findViewById(R.id.new_mood_btn);
        nameTv = (TextView) findViewById(R.id.name_tv);

        signoutBtn = (Button) findViewById(R.id.sign_out_btn);
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        recipeBtn = (Button) findViewById(R.id.recipe_btn);
        recipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().setNavFragment("R");
                Intent i = new Intent(MainActivity.this, RecipesActivity.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });

        favouriteBtn = (Button) findViewById(R.id.favourite_btn);
        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().setNavFragment("F");
                Intent i = new Intent(MainActivity.this, RecipesActivity.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });

        ingredientBtn = (Button) findViewById(R.id.ingredient_btn);
        ingredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyIngredients.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });

        moodBtn = (Button) findViewById(R.id.mood_btn);
        moodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMoodDialog.newInstance().show(getSupportFragmentManager(), "MoodDialog");
            }
        });

        newMoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMoodDialog.newInstance().show(getSupportFragmentManager(), "MoodDialog");
            }
        });
    }

    void logoutUser(){
        if(pref != null && db != null){
            pref.setBoolean(pref.isLoggedIn, false);
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();
        }
    }

    void loadProfile(){
        if(db != null) {
            user = db.getUser();
            imageUrl ="chef";
            nameTv.setText(user.getName());

            if(user.getImage_url() == null || user.getImage_url().isEmpty()){
                Picasso.with(getApplicationContext())
                        .load(R.mipmap.chef)
                        .placeholder(R.drawable.progress_animation)
                        .resize(256,256).centerCrop()
                        .into(profileImage);
            }else {
                Picasso.with(getApplicationContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.progress_animation)
                        .resize(256, 256).centerCrop()
                        .into(profileImage);
            }

        }else{
           logoutUser();
        }
    }

    public void onResume(){
        super.onResume();
        if(AppConfig.isNetworkAvailable(this)){
            loadProfile();
        }else{
            Snackbar.make(profileImage, "No internet connection", Snackbar.LENGTH_INDEFINITE)
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static class ShowMoodDialog extends AppCompatDialogFragment {
        private static final String ITEM_NAME = "name";

        public static ShowMoodDialog newInstance(){
            ShowMoodDialog fragment = new ShowMoodDialog();
            return fragment;
        }

        public ShowMoodDialog(){}

        @Override
        public void onCreate(@Nullable final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            final View view  = inflater.inflate(R.layout.my_mood_layout, null);
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            final CustomMoodAdapter adapter = new CustomMoodAdapter(getActivity().getApplicationContext(),
                    R.layout.mood_item);

            recyclerView.setAdapter(adapter);

            final List<Emotes> emotes = new ArrayList<>();
            Emotes e1 = new Emotes("Happy", 0x1F601);
            Emotes e2 = new Emotes("Sad", 0x1F621);
            Emotes e3 = new Emotes("Frustrated", 0x1F635);
            Emotes e4 = new Emotes("Warm",0x1F60E);
            Emotes e5 = new Emotes("Cold", 0x1F630);
            Emotes e6 = new Emotes("Tired", 0x1F62B);
            Emotes e7 = new Emotes("Sick", 0x1F637);
            Emotes e8 = new Emotes("Festive", 0x2744);
            emotes.add(e1);
            emotes.add(e2);
            emotes.add(e3);
            emotes.add(e4);
            emotes.add(e5);
            emotes.add(e6);
            emotes.add(e7);
            emotes.add(e8);

            adapter.addAll(emotes);

            adapter.setOnItemClickListener(new CustomMoodAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent i = new Intent(getActivity(),RecipesActivity.class);
                    AppController.getInstance().searchKey = emotes.get(position).getLabel();
                    AppController.getInstance().setNavFragment("R");
                    startActivity(i, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }
            });

            return view;
        }
    }
}
