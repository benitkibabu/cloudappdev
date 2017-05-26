package com.cloudappdev.ben.virtualkitchen.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.adapter.CustomIngredientRecyclerAdapter;
import com.cloudappdev.ben.virtualkitchen.rest.APIService;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.main.MainActivity;
import com.cloudappdev.ben.virtualkitchen.models.Ingredient;
import com.cloudappdev.ben.virtualkitchen.models.UPCItem;
import com.cloudappdev.ben.virtualkitchen.models.UPCResponse;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyIngredients extends AppCompatActivity  {

    static User user;

    private static RecyclerView recyclerView;
    private static CustomIngredientRecyclerAdapter adapter;
    static ProgressDialog progressBar;

    static List<Ingredient> myIngredients;
    static List<UPCItem> upcItems;

    static APIService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_my_ingredients);

        service = AppConfig.getAPIService();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = new ProgressDialog(this);

        if(AppController.getInstance().getUser() != null){
            user = AppController.getInstance().getUser();
        }else{
            goBack();
        }

        recyclerView  = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CustomIngredientRecyclerAdapter(this, R.layout.ingredient_item);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CustomIngredientRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                final Ingredient mI = myIngredients.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyIngredients.this);

                builder.setTitle("DELETE?!");
                builder.setMessage("Do you want to remove this Item?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        removeIngredient(mI);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

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
                    .startActivities(ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        }else {
            NavUtils.navigateUpFromSameTask(this);
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
            //goBack();
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static void getMyIngredient(){
        showProgressDialog("Fetching Ingredient");
        service = AppConfig.getAPIService();
        service.fetchMyIngredient(AppController.getInstance().appKey(), user.getId())
                .enqueue(new Callback<List<Ingredient>>() {
                    @Override
                    public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                        hideProgressDialog();
                        if(response.isSuccessful()){
                            myIngredients = response.body();
                            adapter.addAll(myIngredients);
                        }else{
                            Snackbar.make(recyclerView, "Ingredient Not Found!", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                        hideProgressDialog();
                        Log.e("GetMyIngredients", t.toString());
                    }
                });
    }

    private static void showProgressDialog( String m) {
        //if(progressBar == null){
        progressBar.setTitle(m);
        progressBar.show();
        //}
    }

    private static void hideProgressDialog() {
        progressBar.hide();
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
        showProgressDialog("Fetch UPC Item");
        service = AppConfig.getAPIService(AppConfig.UPLOOKUP);
        service.fetchUPCItem(code)
                .enqueue(new Callback<UPCResponse>() {
                    @Override
                    public void onResponse(Call<UPCResponse> call, Response<UPCResponse> response) {
                        hideProgressDialog();
                        if(response.isSuccessful()){
                            upcItems = response.body().getItems();
                            Snackbar.make(recyclerView, "Item found", Snackbar.LENGTH_LONG).show();
                            if(upcItems != null && upcItems.size() > 0) {
                                ShowInsertDialog.newInstance(upcItems.get(0).getTitle()).show(fm, "InserDialog");
                            }
                        }else{
                            Snackbar.make(recyclerView, "Item not found!", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UPCResponse> call, Throwable t) {
                        hideProgressDialog();
                        Log.e("UPC_ITEM", t.toString());
                    }
                });
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
                            Ingredient ing = new Ingredient();
                            ing.setText(text);
                            ing.setWeight(Double.parseDouble(quantity));
                            ing.setUserid(user.getId());

                            addIngredient(ing);

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

    static void addIngredient(final Ingredient ingred) {
        showProgressDialog("Adding Ingredient");
        myIngredients = new ArrayList<>();
        service = AppConfig.getAPIService();
        service.addIngredient(AppController.getInstance().appKey(), ingred)
                .enqueue(new Callback<Ingredient>() {
                    @Override
                    public void onResponse(Call<Ingredient> call, Response<Ingredient> response) {
                        hideProgressDialog();
                        if(response.isSuccessful()){
                            getMyIngredient();
                            Snackbar.make(recyclerView, "You have added your ingredient", Snackbar.LENGTH_LONG).show();
                        }else{
                            Snackbar.make(recyclerView, "Ingredient not added!", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Ingredient> call, Throwable t) {
                        hideProgressDialog();
                        Log.e("NotSucceful", t.toString());
                    }
                });
    }

    void removeIngredient(final Ingredient ingredient) {
        showProgressDialog("Removing Ingredient");
        myIngredients = new ArrayList<>();
        service = AppConfig.getAPIService();
        service.removeIngredient(ingredient.getId(), AppController.getInstance().appKey())
                .enqueue(new Callback<Ingredient>() {
                    @Override
                    public void onResponse(Call<Ingredient> call, Response<Ingredient> response) {
                        hideProgressDialog();
                        if(response.isSuccessful()){
                            getMyIngredient();
                            Snackbar.make(recyclerView, "You have added your ingredient", Snackbar.LENGTH_LONG).show();
                        }else{
                            Snackbar.make(recyclerView, "Item Not Removed!", Snackbar.LENGTH_LONG).show();
                            Log.i("NotSucceful", response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Ingredient> call, Throwable t) {
                        hideProgressDialog();
                        Log.e("NotSucceful", t.toString());
                    }
                });
    }
}
