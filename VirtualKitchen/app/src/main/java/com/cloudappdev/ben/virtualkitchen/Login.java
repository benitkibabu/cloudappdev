package com.cloudappdev.ben.virtualkitchen;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;

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
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    LoginButton fbLoginBtn;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private ProgressDialog mProgressDialog;
    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mProgressDialog = new ProgressDialog(this);

        fbLoginBtn = (LoginButton) findViewById(R.id.facebook_login_button);
        fbLoginBtn.setReadPermissions(Arrays.asList("email", "public_profile", "user_photos"));

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(AppConfig.isNetworkAvailable(Login.this)){
                    handleFacebookSignInResult(loginResult.getAccessToken());
                }else{
                    Snackbar.make(fbLoginBtn, "No internet connection", Snackbar.LENGTH_INDEFINITE)
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
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(AppConfig.isNetworkAvailable(this)){
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken != null) {
                Log.d(TAG, ">>>" + "Signed In");
                handleFacebookSignInResult(accessToken);
            }
            else{
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                if (opr.isDone()) {
                    // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                    // and the GoogleSignInResult will be available instantly.
                    Log.d(TAG, "Got cached sign-in");
                    GoogleSignInResult result = opr.get();
                    handleSignInResult(result);
                } else {
                    // If the user has not previously signed in on this device or the sign-in has expired,
                    // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                    // single sign-on will occur in this branch.
                    showProgressDialog();
                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(GoogleSignInResult googleSignInResult) {
                            hideProgressDialog();
                            handleSignInResult(googleSignInResult);
                        }
                    });
                }
            }
        }else{
            Snackbar.make(fbLoginBtn, "No internet connection", Snackbar.LENGTH_INDEFINITE)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

                String name = acct.getDisplayName();
                String email = acct.getEmail();
                String id = acct.getId();

                String picUrl = acct.getPhotoUrl().toString();
                Log.w("GImg", picUrl);
                User user = new User("Facebook", id, name, email, picUrl);
            try {
                postStudent(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void handleFacebookSignInResult(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Log.d("FBLogin Response ", response.toString());
                        try {
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String id = object.getString("id");

                            JSONObject picObj = object.getJSONObject("picture");
                            JSONObject picData = picObj.getJSONObject("data");
                           // String picUrl = picData.getString("url");
                            String picUrl = "https://graph.facebook.com/"+id+"/picture?type=large";

                            User user = new User("Facebook", id, name, email, picUrl);
                            postStudent(user);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,picture,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    //Post Request
    void postStudent(final User user) throws JSONException {
        showProgressDialog();
        final String TAG = "Log User";

        JSONObject params = new JSONObject();
       // params.put("app_key", AppController.getInstance().appKey());
        params.put("logintype", user.getLoginType());
        params.put("userid", user.getUserid());
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        params.put("imageurl", user.getImageUrl());

        Log.d(TAG, params.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                AppConfig.INTERNAL_USERS_API,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            Intent it = new Intent(Login.this, MainActivity.class);
                            User u = new User(response.getInt("id"), response.getString("logintype"),
                                    response.getString("userid"), response.getString("name"),
                                    response.getString("email"), response.getString("imageurl"));
                            AppController.getInstance().setUser(u);
                            updateUI(it);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                }

                if (error instanceof TimeoutError) {
                    Log.e("Volley", "TimeoutError");
                }else if(error instanceof NoConnectionError){
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                headers.put("app_key", AppController.getInstance().appKey());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }else{
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void updateUI(Intent i){
        hideProgressDialog();
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
