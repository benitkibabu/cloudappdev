package com.cloudappdev.ben.virtualkitchen.main;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.helper.AppPreference;
import com.cloudappdev.ben.virtualkitchen.helper.SQLiteHandler;
import com.cloudappdev.ben.virtualkitchen.rest.APIService;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdditionalInfo extends AppCompatActivity {

    EditText username, password, confirmPassword;

    private ProgressDialog mProgressDialog;

    List<User> users;
    boolean usernameValid = true;

    User temp;
    AppPreference pref;
    SQLiteHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_additional_info);

        pref = new AppPreference(this);
        db = new SQLiteHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressDialog = new ProgressDialog(this);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);

        getAllUser();

        temp = new User();

        if(getIntent().hasExtra("user")) {
            temp = (User) getIntent().getSerializableExtra("user");
        }
        else{
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!username.getText().toString().isEmpty() &&
                        password.getText().toString().equals(confirmPassword.getText().toString())) {

                    for(User u : users ){
                        if(u.getUsername().equals(username.getText().toString())){
                            Snackbar.make(view, "Username is already taken! Try another",
                                    Snackbar.LENGTH_LONG).show();
                            usernameValid = false;
                            break;
                        }
                    }
                    if(usernameValid) {
                        temp.setUsername(username.getText().toString());
                        temp.setEncrypted_password(password.getText().toString());
                        postUser(temp);
                    }
                }else{
                    Snackbar.make(view, "Make sure to fill out all fields!",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    //Post Request
    void postUser(final User u) {
        showProgressDialog();
        db.creatUser(u);
        pref.setBoolean(pref.isLoggedIn, true);
        launchMainActivity();
    }

    void getAllUser(){
        users = new ArrayList<>();
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
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }
}
