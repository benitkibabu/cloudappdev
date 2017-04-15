package com.cloudappdev.ben.virtualkitchen.main;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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

import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private ProgressDialog mProgressDialog;

    Button signinBtn, registerBtn;

    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.password);

        signinBtn = (Button) findViewById(R.id.signinBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);

        mProgressDialog = new ProgressDialog(this);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser(email.getText().toString(), password.getText().toString());
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, RegisterActivity.class);
                startActivity(i, ActivityOptions.makeSceneTransitionAnimation(Login.this).toBundle());
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                    public void onResponse(JSONObject job) {
                        Log.d("Response", job.toString());
                        //getUser(user);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                NetworkResponse networkResponse = error.networkResponse;

                //getUser(user);

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

    public void getUser(final String email, final String password){
        StringRequest request = new StringRequest(Request.Method.GET, AppConfig.INTERNAL_USERS_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject obj = null;
                            for(int i=0; i<array.length(); i++){
                                JSONObject temp = array.getJSONObject(i);
                                if(temp.getString("email").equals(email) &&
                                        temp.getString("password").equalsIgnoreCase(password)){
                                    obj = temp;
                                    break;
                                }
                            }

                            if(obj == null){
                                //User not found
                               // postUser(user);
                            }else {
                                Intent it = new Intent(Login.this, MainActivity.class);
                                User u = new User(obj.getInt("id"), obj.getString("logintype"),
                                        obj.getString("userid"), obj.getString("name"),
                                        obj.getString("email"), obj.getString("imageurl"));
                                AppController.getInstance().setUser(u);
                                updateUI(it);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(request);
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

}
