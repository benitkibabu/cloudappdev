package com.cloudappdev.ben.virtualkitchen.main;

import android.app.ActivityOptions;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.activities.MyIngredients;
import com.cloudappdev.ben.virtualkitchen.activities.RecipesActivity;
import com.cloudappdev.ben.virtualkitchen.adapter.CustomMoodAdapter;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.Emotes;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button recipeBtn, signoutBtn, ingredientBtn, favouriteBtn, moodBtn;
    TextView nameTv;
    CircleImageView profileImage;

    private static final String TAG = "SignOutActivity";
    public String imgurl;
    boolean fbLogout = false, gLogout = false;
    User data;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        profileImage = (CircleImageView) findViewById(R.id.profile_img);
        nameTv = (TextView) findViewById(R.id.name_tv);

        signoutBtn = (Button) findViewById(R.id.sign_out_btn);
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookSignOut();
            }
        });

        recipeBtn = (Button) findViewById(R.id.recipe_btn);
        recipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().setNavFragement("R");
                Intent i = new Intent(MainActivity.this, RecipesActivity.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });

        favouriteBtn = (Button) findViewById(R.id.favourite_btn);
        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().setNavFragement("F");
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
    }
    void loadProfile(){
        if(AppController.getInstance().getUser() != null) {

            data = AppController.getInstance().getUser();
            imgurl = "https://graph.facebook.com/"+data.getUserid()+"/picture?type=large";
            nameTv.setText(data.getName());

            Picasso.with(getApplicationContext())
                    .load(imgurl)
                    .resize(256,256).centerCrop()
                    .into(profileImage);

        }else{
            facebookSignOut();
            //googleSignOut();
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

    private void facebookSignOut(){
        if (AccessToken.getCurrentAccessToken() == null) {

            fbLogout = true;
        }else{
            LoginManager.getInstance().logOut();
            fbLogout = true;
        }

        if(fbLogout){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
            new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    // [START_EXCLUDE]
                    // [END_EXCLUDE]
                }
            });
            AppController.getInstance().setUser(null);
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();
        }
// code for revoking access of facebook
// new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
//                .Callback() {
//            @Override
//            public void onCompleted(GraphResponse graphResponse) {
//                LoginManager.getInstance().logOut();
//                fbLogout = true;
//            }
//        }).executeAsync();
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

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                setStyle(STYLE_NO_TITLE, R.style.KitchenTheme);
//            } else {
//                setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
//            }

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //LayoutInflater inflater = getActivity().getLayoutInflater();

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
                    AppController.getInstance().setNavFragement("R");
                    startActivity(i, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                }
            });

            return view;
        }

    }
}
