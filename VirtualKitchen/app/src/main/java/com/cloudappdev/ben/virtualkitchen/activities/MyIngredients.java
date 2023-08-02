package com.cloudappdev.ben.virtualkitchen.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.adapter.CustomIngredientRecyclerAdapter;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.helper.AppPreference;
import com.cloudappdev.ben.virtualkitchen.helper.SQLiteHandler;
import com.cloudappdev.ben.virtualkitchen.main.MainActivity;
import com.cloudappdev.ben.virtualkitchen.models.Ingredient;
import com.cloudappdev.ben.virtualkitchen.models.UPCItem;
import com.cloudappdev.ben.virtualkitchen.models.UPCResponse;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.cloudappdev.ben.virtualkitchen.rest.APIService;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyIngredients extends AppCompatActivity {

    private User user;
    private RecyclerView recyclerView;
    private CustomIngredientRecyclerAdapter adapter;
    private ProgressDialog progressBar;

    static List<Ingredient> myIngredients;

    AppPreference pref;
    SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

        setContentView(R.layout.activity_my_ingredients);

        pref = new AppPreference(this);
        db = new SQLiteHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView  = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CustomIngredientRecyclerAdapter(this, R.layout.ingredient_item);
        recyclerView.setAdapter(adapter);


        if(db != null){
            user = db.getUser();
            getMyIngredient();
        }else{
            goBack();
        }

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
            if(db != null){
                user = db.getUser();
                getMyIngredient();
            }else{
                goBack();
            }
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

    private void getMyIngredient(){
        showProgressDialog("Loading Items");
        myIngredients = new ArrayList<>();
        APIService service = AppConfig.getAPIService();
        service.fetchMyIngredient(AppController.getInstance().appKey(), user.getId())
                .enqueue(new Callback<List<Ingredient>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Ingredient>> call, @NonNull Response<List<Ingredient>> response) {
                        hideProgressDialog();
                        if(response.isSuccessful()){
                            myIngredients = response.body();
                            adapter.addAll(myIngredients);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Ingredient>> call, @NonNull Throwable t) {
                        hideProgressDialog();
                        Log.e("GetMyIngredients", t.toString());
                    }
                });
    }

    private void showProgressDialog( String m) {
        if(progressBar == null){
            progressBar = new ProgressDialog(this);
            progressBar.setTitle(m);
            progressBar.show();
        }else{
            progressBar.setTitle(m);
            progressBar.show();
        }
    }

    private void hideProgressDialog() {
        progressBar.hide();
    }

    /*
        Reference: https://github.com/dm77/barcodescanner
        Created By: DM77
     */
    void showEditDialog(){
        DialogFragment newFragment =  SimpleScannerFragment.newInstance(user);
        newFragment.show(getSupportFragmentManager(), "show_scanner");
    }

    public static class SimpleScannerFragment extends AppCompatDialogFragment implements ZXingScannerView.ResultHandler {
        private ZXingScannerView mScannerView;
        private User user;


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

            if(!getArguments().isEmpty()) {
                user = (User) getArguments().getSerializable("user");
            }
        }

        public static SimpleScannerFragment newInstance(User user){
            SimpleScannerFragment s = new SimpleScannerFragment();
            Bundle args = new Bundle();
            args.putSerializable("user", user);
            s.setArguments(args);
            return s;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mScannerView = new ZXingScannerView(getActivity());
            this.getDialog().setTitle("Scan Product");
            mScannerView.setAutoFocus(true);
            mScannerView.setFocusableInTouchMode(true);
            mScannerView.setFlash(false);
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

        private void getProductByUPC(String code,final FragmentManager fm){
            APIService service = AppConfig.getAPIService(AppConfig.UPLOOKUP);
            service.fetchUPCItem(code)
            .enqueue(new Callback<UPCResponse>() {
                @Override
                public void onResponse(@NonNull Call<UPCResponse> call, @NonNull Response<UPCResponse> response) {
                    if(response.isSuccessful()){
                        if(response.body().getItems() != null) {
                            List<UPCItem> upcItems = response.body().getItems();
                            Log.d("UPCResult", response.toString());
                            if (upcItems != null && upcItems.size() > 0) {
                                ShowInsertDialog.newInstance(upcItems.get(0).getTitle(), user).show(fm, "InsertDialog");
                            }
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Item was not found", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),
                                "You have added your ingredient", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UPCResponse> call, @NonNull Throwable t) {
                    Log.e("UPC_ITEM", t.toString());
                }
            });
        }
    }


    public static class ShowInsertDialog extends AppCompatDialogFragment{
        LinearLayout layout;
        private User user;

        public static ShowInsertDialog newInstance(String title, User user){
            ShowInsertDialog fragment = new ShowInsertDialog();
            Bundle arg = new Bundle();
            arg.putString("code", title);
            arg.putSerializable("user", user);
            fragment.setArguments(arg);
            return fragment;
        }

        public ShowInsertDialog(){}

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view  = inflater.inflate(R.layout.dialog_add_ingredient, null);

            final TextView title = (TextView) view.findViewById(R.id.title);
            Button saveBtn = (Button) view.findViewById(R.id.saveBtn);
            Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
            layout = (LinearLayout) view.findViewById(R.id.loading_tag);

            layout.setVisibility(View.INVISIBLE);

            if(!getArguments().isEmpty()) {
                title.setText(getArguments().getString("code"));
                user =(User) getArguments().getSerializable("user");
            }

            final EditText qField = (EditText) view.findViewById(R.id.q_field);

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = title.getText().toString();
                    String quantity = qField.getText().toString();

                    //Call Save Ingredient
                    Ingredient ing = new Ingredient();
                    ing.setText(text);
                    ing.setWeight(Double.parseDouble(quantity));
                    ing.setUserid(user.getId());

                    addIngredient(ing);
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowInsertDialog.this.getDialog().cancel();
                }
            });

            builder.setIcon(R.mipmap.wine_bottle);
            builder.setTitle(R.string.add_ingredient);
            builder.setCancelable(false);
            builder.setView(view);

            return builder.create();
        }
        void addIngredient(final Ingredient ingredient) {
            layout.setVisibility(View.VISIBLE);
            APIService service = AppConfig.getAPIService();
            service.addIngredient(AppController.getInstance().appKey(), ingredient)
                    .enqueue(new Callback<Ingredient>() {
                        @Override
                        public void onResponse(@NonNull Call<Ingredient> call, @NonNull Response<Ingredient> response) {
                            layout.setVisibility(View.INVISIBLE);
                            if(response.isSuccessful()){
                                Toast.makeText(getActivity().getBaseContext(), "Item Saved!",
                                        Toast.LENGTH_LONG).show();
                                ShowInsertDialog.this.getDialog().cancel();
                            }else{
                                Toast.makeText(getActivity().getBaseContext(), "Item Not Saved!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<Ingredient> call, @NonNull Throwable t) {
                            layout.setVisibility(View.INVISIBLE);
                            Log.e("NotSucceful", t.toString());
                        }
                    });
        }
    }

    void removeIngredient(final Ingredient ingredient) {
        showProgressDialog("Removing Item!...");
        myIngredients = new ArrayList<>();

        APIService service = AppConfig.getAPIService();
        service.removeIngredient(ingredient.getId(), AppController.getInstance().appKey())
                .enqueue(new Callback<Ingredient>() {
                    @Override
                    public void onResponse(@NonNull Call<Ingredient> call, @NonNull Response<Ingredient> response) {
                        hideProgressDialog();
                        if(response.isSuccessful()){
                            Snackbar.make(recyclerView, "Item Removed", Snackbar.LENGTH_LONG).show();
                            getMyIngredient();
                        }else{
                            Snackbar.make(recyclerView, "Item Not Removed!", Snackbar.LENGTH_LONG).show();
                            Log.e("NotSuccessful", response.toString());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Ingredient> call, @NonNull Throwable t) {
                        hideProgressDialog();
                        Log.e("NotSuccessful", t.toString());
                    }
                });
    }
}
