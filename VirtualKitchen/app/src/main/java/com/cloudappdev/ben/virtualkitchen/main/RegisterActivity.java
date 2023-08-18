package com.cloudappdev.ben.virtualkitchen.main;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.helper.AppPreference;
import com.cloudappdev.ben.virtualkitchen.helper.SQLiteHandler;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button fbLoginBtn;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private ProgressDialog mProgressDialog;
    EditText fullname, username;
    Button registerBtn, signBtn;

    List<User> users;
    boolean usernameValid = true;

    AppPreference pref;
    SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_register);
        pref = new AppPreference(this);
        db = new SQLiteHandler(this);

        if(pref.getBooleanVal(pref.isLoggedIn)){
            User u = db.getUser();
            if(u != null) {
                launchMainActivity();
            }else{
                pref.setBoolean(pref.isLoggedIn, false);
            }
        }

        fullname = (EditText) findViewById(R.id.fullname);
        username = (EditText) findViewById(R.id.username);

        mProgressDialog = new ProgressDialog(this);

        getAllUser();

        signBtn = (Button) findViewById(R.id.signinBtn);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, Login.class);
                startActivity(i,  ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
                finish();
            }
        });

        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateField(username)){

                    for(User u : users){
                        if(u.getUsername().equals(username.getText().toString())){
                            usernameValid = false;
                            break;
                        }
                    }

                    if(usernameValid) {
                        User u = new User();
                        u.setName(fullname.getText().toString());
                        u.setUsername(username.getText().toString());
                        postUser(u);
                    }else{
                        Snackbar.make(v, R.string.username_taken_error,
                                Snackbar.LENGTH_LONG).show();
                    }
                }else{
                    Snackbar.make(v, getString(R.string.empty_field_error),
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    boolean validateField(EditText editText){
        if(editText.getText().toString().isEmpty()){
            editText.setError(getString(R.string.empty_field_error));
            return false;
        }
        else
            return true;
    }

    void getAllUser(){
        users = new ArrayList<>();
    }

    void postUser(final User u) {
        showProgressDialog("Registering!....");
        db.creatUser(u);
        pref.setBoolean(pref.isLoggedIn, true);
        launchMainActivity();
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
        }else{
            hideProgressDialog();
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
    }

    private void showProgressDialog(String t) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(t);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }else{
            mProgressDialog.setMessage(t);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    public void launchMainActivity(){
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(fbLoginBtn, "Failed to Authenticate", Snackbar.LENGTH_LONG).show();
    }
}
