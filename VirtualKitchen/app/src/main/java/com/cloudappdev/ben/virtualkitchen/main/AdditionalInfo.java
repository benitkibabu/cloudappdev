package com.cloudappdev.ben.virtualkitchen.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdditionalInfo extends AppCompatActivity {

    EditText password, confirmPassword;

    private ProgressDialog mProgressDialog;

    User temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_additional_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressDialog = new ProgressDialog(this);

        temp = new User();

        if(getIntent().hasExtra("user")){
            temp = (User) getIntent().getSerializableExtra("user");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                    temp.setPassword(password.getText().toString());

                    try {
                        postUser(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                }
            }
        });
    }

    //Post Request
    void postUser(final User user) throws JSONException {
        showProgressDialog();
        final String TAG = "Log User";

        JSONObject params = new JSONObject();
        // params.put("app_key", AppController.getInstance().appKey());
        params.put("logintype", user.getLoginType());
        params.put("userid", user.getUserid());
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
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

}
