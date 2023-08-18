package com.cloudappdev.ben.virtualkitchen.main;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.helper.AppPreference;
import com.cloudappdev.ben.virtualkitchen.helper.SQLiteHandler;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.cloudappdev.ben.virtualkitchen.rest.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private ProgressDialog mProgressDialog;
    Button signinBtn, registerBtn;
    AppPreference pref;
    SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_login);
        pref = new AppPreference(this);
        db = new SQLiteHandler(this);

        signinBtn = (Button) findViewById(R.id.signinBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        mProgressDialog = new ProgressDialog(this);

        if(pref.getBooleanVal(pref.isLoggedIn)){
            User u = db.getUser();
            if(u != null) {
                launchMainActivity();
            }else{
                pref.setBoolean(pref.isLoggedIn, false);
            }
        }

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInOnClick(v);
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

    void signInOnClick(View v){
        signIn();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void signIn(){
        showProgressDialog();

        if(db.getUser() != null) {
            pref.setBoolean(pref.isLoggedIn, true);
            launchMainActivity();
        }else {
            Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
        }
        hideProgressDialog();
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

    public void launchMainActivity(){
        hideProgressDialog();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

}
