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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button recipeBtn, signoutBtn;
    TextView nameTv;
    ImageButton profileImage;

    private static final String TAG = "SignOutActivity";
    private GoogleApiClient mGoogleApiClient;
    public String imgurl;
    boolean fbLogout = false, gLogout = false;
    Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        profileImage = (ImageButton) findViewById(R.id.profile_img);
        nameTv = (TextView) findViewById(R.id.name_tv);

        if(getIntent().hasExtra("User")) {
            if (!getIntent().getExtras().isEmpty()) {
                data = getIntent().getBundleExtra("User");
                imgurl = data.getString("ImageUri");
                nameTv.setText(data.getString("DisplayName"));

                Picasso.with(getApplicationContext())
                        .load(imgurl)
                        .resize(256,256).centerCrop()
                        .into(profileImage);
            }
        }else{
            facebookSignOut();
            googleSignOut();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signoutBtn = (Button) findViewById(R.id.sign_out_btn);
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignOut();
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

//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            return  BitmapFactory.decodeStream(input);
//        } catch (IOException e) {
//
//            return null;
//        }
//    }
//
//    private class FetchFilesTask extends AsyncTask<String, Void, Bitmap> {
//        protected void onPreExecute(){
//        }
//        protected Bitmap doInBackground(String... args) {
//            return getBitmapFromURL(imgurl);
//        }
//        protected void onPostExecute(Bitmap m_bitmap) {
//            if(m_bitmap != null){
//                profileImage.setImageBitmap(getResizedBitmap(m_bitmap, 512, 512));
//            }
//        }
//    }
//
//    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        return Bitmap.createBitmap(bm, 0, 0, width, height,
//                matrix, false);
//    }

    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        gLogout = true;
                        facebookSignOut();
                    }
                });
    }
    private void facebookSignOut(){
        if (AccessToken.getCurrentAccessToken() == null) {
            fbLogout = true;
        }else{
            LoginManager.getInstance().logOut();
            fbLogout = true;
        }

        if(gLogout){
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
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
