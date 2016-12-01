package com.cloudappdev.ben.virtualkitchen.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.cloudappdev.ben.virtualkitchen.MainActivity;
import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.adapter.CustomIngredientRecyclerAdapter;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.Ingredient;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MyIngredients extends AppCompatActivity  {

    static User user;

    private static RecyclerView recyclerView;
    private static CustomIngredientRecyclerAdapter adapter;
    private static ProgressDialog mProgressDialog;

    static List<Ingredient> myIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ingredients);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(AppController.getInstance().getUser() != null){
            user = AppController.getInstance().getUser();
        }else{
            goBack();
        }


        mProgressDialog = new ProgressDialog(this);

        recyclerView  = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CustomIngredientRecyclerAdapter(this, R.layout.ingredient_item);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume(){
        super.onResume();
        if(AppConfig.isNetworkAvailable(this)){
            getMyIngredient();
        }else{
            Snackbar.make(recyclerView, "No internet connection", Snackbar.LENGTH_INDEFINITE)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_myingredients, menu);
        return true;
    }

    void goBack(){
        Intent upIntent = new Intent(this, MainActivity.class);
        if(NavUtils.shouldUpRecreateTask(this, upIntent)){
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
            finish();
        }else {
            NavUtils.navigateUpTo(this, upIntent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            //Show Camera here for scanning items
            showEditDialog();
            return true;
        }
        else if (id == R.id.action_stores) {
            Intent i = new Intent(MyIngredients.this, MapsActivity.class);
            startActivity(i);
            return true;
        }
        else if(id == android.R.id.home){
            goBack();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void getMyIngredient(){
        showProgressDialog("Getting Ingredient");
        final String TAG = "Get Ingredients";
        Uri url = Uri.parse(AppConfig.INTERNAL_INGREDIENT_API)
                .buildUpon()
                //.appendQueryParameter("userid", String.valueOf(user.getId()))
                .build();

        Log.w("Url", url.toString());

        JsonArrayRequest request = new JsonArrayRequest(url.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hideProgressDialog();
                        Log.w(TAG, response.toString());
                        myIngredients = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject j = response.getJSONObject(i);
                                if(j.getInt("userid") == user.getId()) {
                                    Ingredient in = new Ingredient(j.getInt("id"),
                                            j.getString("text"), j.getDouble("quantity"),
                                            j.getInt("userid"));

                                    myIngredients.add(in);
                                }
                            }
                            adapter.addAll(myIngredients);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                headers.put("app_key", AppController.getInstance().appKey());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    private static void showProgressDialog( String m) {
        if (mProgressDialog == null) {
            mProgressDialog.setMessage(m);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }else{
            mProgressDialog.setMessage(m);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }
    }

    private static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /*

    Reference: https://github.com/dm77/barcodescanner
    Created By: DM77
     */
    void showEditDialog(){
        DialogFragment newFragment =  SimpleScannerFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "show_scanner");
    }

    public static class SimpleScannerFragment extends AppCompatDialogFragment implements ZXingScannerView.ResultHandler {
        private ZXingScannerView mScannerView;


        public SimpleScannerFragment(){
        }

        @Override
        public void onCreate(@Nullable final Bundle savedInstanceState) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
            } else {
                setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
            }
            super.onCreate(savedInstanceState);

        }

        public static SimpleScannerFragment newInstance(){
            SimpleScannerFragment fr = new SimpleScannerFragment();
            return fr;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mScannerView = new ZXingScannerView(getActivity());
            this.getDialog().setTitle("Scan Product");
            mScannerView.setAutoFocus(true);
            //mScannerView.setFlash(false);
            return mScannerView;
        }

        @Override
        public void onResume() {
            super.onResume();
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();          // Start camera on resume
        }

        @Override
        public void onPause() {
            super.onPause();
            mScannerView.stopCamera();           // Stop camera on pause
        }

        @Override
        public void handleResult(Result rawResult) {
            // Do something with the result here
            Log.v("Content", rawResult.getText()); // Prints scan results
            Log.v("format", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

            // If you would like to resume scanning, call this method below:
            //mScannerView.resumeCameraPreview(this);
            getProductByUPC(rawResult.getText(), getActivity().getSupportFragmentManager());
            dismiss();
        }


    }
    private static void getProductByUPC(String code,final FragmentManager fm){
        showProgressDialog("Get Product Info");
        final String TAG = "GetProducName";
        Uri url = Uri.parse( AppConfig.UPLOOKUP)
                .buildUpon()
                .appendQueryParameter("upc", code)
                .build();

        Log.w("Url", url.toString());

        StringRequest request = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w("RESPONSE", response);
                        hideProgressDialog();

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("items");
                            if(array.length()> 0) {
                                JSONObject item = array.getJSONObject(0);
                                String title = item.getString("title");

                                Snackbar.make(recyclerView,
                                        title, Snackbar.LENGTH_LONG).show();

                                ShowInsertDialog.newInstance(title).show(fm, "InserDialog");
                                //SimpleScannerFragment.this.getDialog().dismiss();
                            }else{
                                Snackbar.make(recyclerView,
                                        "Items Not Found", Snackbar.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Updates Error", "Error " + error.getMessage());
                hideProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(request, TAG);
    }

    public static class ShowInsertDialog extends AppCompatDialogFragment{

        public static ShowInsertDialog newInstance(String title){
            ShowInsertDialog fragment = new ShowInsertDialog();
            Bundle arg = new Bundle();
            arg.putString("code", title);
            fragment.setArguments(arg);
            return fragment;
        }

        public ShowInsertDialog(){}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            final View view  = inflater.inflate(R.layout.dialog_add_ingredient, null);
            final TextView title = (TextView) view.findViewById(R.id.title);

            if(!getArguments().isEmpty()) {

                title.setText(getArguments().getString("code"));
            }

            final EditText qField = (EditText) view.findViewById(R.id.q_field);

            builder.setIcon(R.mipmap.wine_bottle);
            builder.setTitle(R.string.add_ingredient);
            builder.setCancelable(false);
            builder.setView(view).setPositiveButton(R.string.save_ingredient,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            String text = title.getText().toString();
                            String quantity = qField.getText().toString();

                            //Call Save Ingredient
                            Ingredient ing = new Ingredient(text, Double.parseDouble(quantity),
                                    user.getId());
                            try {
                                addIngredient(ing);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ShowInsertDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }

    static void addIngredient(final Ingredient ingred) throws JSONException {
        showProgressDialog("Add Ingredient");
        final String TAG = "Saving Recipe";

        JSONObject jobj = new JSONObject();

        jobj.put("text", ingred.getText());
        jobj.put("quantity", ingred.getQuantity());
        jobj.put("userid", ingred.getUserid());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
                AppConfig.INTERNAL_INGREDIENT_API,
                jobj,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,response.toString());
                        Snackbar.make(recyclerView,response.toString(), Snackbar.LENGTH_LONG)
                                .show();
                        hideProgressDialog();
                        getMyIngredient();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                headers.put("app_key", AppController.getInstance().appKey());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(objectRequest, TAG);
    }
}
