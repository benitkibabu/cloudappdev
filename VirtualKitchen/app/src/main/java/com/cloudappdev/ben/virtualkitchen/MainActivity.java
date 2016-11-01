package com.cloudappdev.ben.virtualkitchen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cloudappdev.ben.virtualkitchen.activities.RecipesActivity;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button recipeBtn, signoutBtn;
    TextView nameTv;
    ImageButton profileImage;

    private static final String TAG = "SignOutActivity";
    public String imgurl;
    boolean fbLogout = false, gLogout = false;
    User data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        profileImage = (ImageButton) findViewById(R.id.profile_img);
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
                Intent i = new Intent(MainActivity.this, RecipesActivity.class);
                i.putExtra("User", data);
                startActivity(i);
            }
        });
    }
    void loadProfile(){
        if(AppController.getUser() != null) {

            data = AppController.getUser();
            imgurl = data.getImageUrl();
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
        loadProfile();
    }

    private void facebookSignOut(){
        if (AccessToken.getCurrentAccessToken() == null) {
            fbLogout = true;
        }else{
            LoginManager.getInstance().logOut();
            fbLogout = true;
        }

        if(fbLogout){
            AppController.setUser(null);
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
}
