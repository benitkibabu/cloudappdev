package com.cloudappdev.ben.virtualkitchen.main;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.helper.AppPreference;
import com.cloudappdev.ben.virtualkitchen.helper.SQLiteHandler;
import com.cloudappdev.ben.virtualkitchen.rest.APIService;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    LoginButton fbLoginBtn;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private ProgressDialog mProgressDialog;
    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;

    EditText fullname, email, username, password;
    Button registerBtn, signBtn;

    APIService service;

    List<User> users;
    boolean usernameValid = true;

    AppPreference pref;
    SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
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

        service = AppConfig.getAPIService();

        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mProgressDialog = new ProgressDialog(this);

        getAllUser();

        fbLoginBtn = (LoginButton) findViewById(R.id.facebook_login_button);
        fbLoginBtn.setReadPermissions(Arrays.asList("email", "public_profile", "user_photos"));

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(AppConfig.isNetworkAvailable(RegisterActivity.this)){
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
                if(validateField(email)&&validateField(username)&&
                        validateField(password)){

                    for(User u : users){
                        if(u.getUsername().equals(username.getText().toString())){
                            usernameValid = false;
                            break;
                        }
                    }

                    if(usernameValid) {
                        User u = new User();
                        u.setLogin_type("Internal");
                        u.setEmail(email.getText().toString());
                        u.setName(fullname.getText().toString());
                        u.setEncrypted_password(password.getText().toString());
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
        service.getAllUser(AppController.getInstance().appKey())
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if(response.isSuccessful()){
                            users = response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.e("AllUsers", t.toString());
                    }
                });
    }

    void postUser(final User u) {
        showProgressDialog("Registering!....");
        service.registerUser(getString(R.string.vk_app_key), u).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                hideProgressDialog();
                if(response.isSuccessful()){
                    User user = response.body();
                    db.creatUser(user);
                    pref.setBoolean(pref.isLoggedIn, true);
                    launchMainActivity();
                }else{
                    Log.d("Response", response.toString());
                    Toast.makeText(RegisterActivity.this, "Failed to register",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgressDialog();
                Log.e("REPFAILURE", t.toString());
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
               // handleFacebookSignInResult(accessToken);
            }
            else{
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                if (opr.isDone()) {
                    // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                    // and the GoogleSignInResult will be available instantly.
                    Log.d(TAG, "Got cached sign-in");
                    GoogleSignInResult result = opr.get();
                   // handleSignInResult(result);
                } else {
                    // If the user has not previously signed in on this device or the sign-in has expired,
                    // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                    // single sign-on will occur in this branch.

                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(GoogleSignInResult googleSignInResult) {
                            hideProgressDialog();
                            //handleSignInResult(googleSignInResult);
                        }
                    });
                }
            }
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
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        showProgressDialog("Google Signin...");
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            hideProgressDialog();
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String id = acct.getId();

            String picUrl = acct.getPhotoUrl().toString();
            Log.w("GImg", picUrl);
            User user = new User();
            user.setLogin_type("Google");
            user.setLogin_id(id);
            user.setName(name);
            user.setEmail(email);
            user.setImage_url(picUrl);

            launchMoreInfoPage(user);
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    private void handleFacebookSignInResult(AccessToken accessToken){
        showProgressDialog("Facebook Signin!...");
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Log.d("FBLogin Response ", response.toString());
                        hideProgressDialog();
                        try {
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String id = object.getString("id");

                            JSONObject picObj = object.getJSONObject("picture");
                            String picUrl = "https://graph.facebook.com/"+id+"/picture?type=large";

                            User user = new User();
                            user.setLogin_type("Facebook");
                            user.setLogin_id(id);
                            user.setName(name);
                            user.setEmail(email);
                            user.setImage_url(picUrl);

                            launchMoreInfoPage(user);

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

    void launchMoreInfoPage(User user){
        Intent i = new Intent(RegisterActivity.this, AdditionalInfo.class);
        i.putExtra("user", user);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
        finish();
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
