package com.cloudappdev.ben.virtualkitchen.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.android.volley.toolbox.StringRequest;
import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.activities.RecipeDetails;
import com.cloudappdev.ben.virtualkitchen.adapter.RecipeRecycleViewAdapter;
import com.cloudappdev.ben.virtualkitchen.app.AppConfig;
import com.cloudappdev.ben.virtualkitchen.app.AppController;
import com.cloudappdev.ben.virtualkitchen.models.Ingredient;
import com.cloudappdev.ben.virtualkitchen.models.Recipe;
import com.cloudappdev.ben.virtualkitchen.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RecipeListFragment extends Fragment {


    private User user;

   // private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecipeRecycleViewAdapter adapter;
    String API_ID;
    String API_KEY;

    List<Recipe> recipeList;

    public RecipeListFragment() {
    }

    public static RecipeListFragment newInstance() {
        RecipeListFragment fragment = new RecipeListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppController.getUser() != null) {
            user = AppController.getUser();
        }
        API_ID = getString(R.string.edamam_api_id);
        API_KEY = getString(R.string.edamam_api_key);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        recyclerView  = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        adapter = new RecipeRecycleViewAdapter(getActivity(), R.layout.recipe_item);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecipeRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity().getBaseContext(), RecipeDetails.class);
                Recipe r = recipeList.get(position);
                i.putExtra("Recipe", r);
                startActivity(i);
            }
        });

        return view;
    }

    void loadList(){
        recipeList = new ArrayList<>();
        getRecipe("chicken", API_KEY, API_ID, getContext());
    }

    public List<Recipe> getRecipe(final String query, final String API_KEY, final String API_ID, final Context context){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Data...");

        final String TAG = "Get Recipe";

        dialog.show();

        Uri url = Uri.parse( AppConfig.RECIPE_API)
                .buildUpon()
                .appendQueryParameter("q", query)
                .appendQueryParameter("app_id", API_ID)
                .appendQueryParameter("app_key", API_KEY)
                .build();

        StringRequest request = new StringRequest(Request.Method.GET,url.toString(), new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                Log.d("API RESULT", response);
                try{
                    JSONObject obj = new JSONObject(response);
                    JSONArray hits = obj.getJSONArray("hits");
                    for(int i = 0; i<hits.length(); i++) {
                        JSONObject r = hits.getJSONObject(i);
                        JSONObject re = r.getJSONObject("recipe");
                        JSONArray dL = re.getJSONArray("dietLabels");
                        JSONArray hL = re.getJSONArray("healthLabels");
                        JSONArray ca = re.getJSONArray("cautions");
                        JSONArray iL = re.getJSONArray("ingredientLines");
                        JSONArray ing = re.getJSONArray("ingredients");

                        ArrayList<Ingredient> ingredientsList = new ArrayList<>();
                        String dietLabels = "";
                        String healthLabels = "";
                        String cautions = "";
                        String ingredientLines = "";

                        for(int x = 0; x < dL.length(); x++){
                            dietLabels += dL.getString(x)+", ";
                        }
                        for(int x = 0; x < hL.length(); x++){
                            healthLabels += hL.getString(x)+", ";
                        }
                        for(int x = 0; x < ca.length(); x++){
                            cautions += ca.getString(x)+", ";
                        }
                        for(int x = 0; x < iL.length(); x++){
                            ingredientLines += iL.getString(x)+", ";
                        }
                        for(int x = 0; x < ing.length(); x++){
                            JSONObject val = ing.getJSONObject(x);
                            Ingredient ingr = new Ingredient(val.getString("text"),
                                    val.getDouble("quantity"),val.getString("measure"),
                                    val.getDouble("weight"),  val.getString("food"));
                            ingredientsList.add(ingr);
                        }


                        Recipe rec = new Recipe(re.getString("uri"), re.getString("label"), re.getString("image"),
                                re.getString("source"), re.getString("url"),re.getString("shareAs"),
                                re.getDouble("yield"), dietLabels, healthLabels, cautions, ingredientLines, ingredientsList,
                                re.getDouble("calories"), re.getDouble("totalWeight"));
                        recipeList.add(rec);
                    }
                    adapter.addAll(recipeList);

                } catch (JSONException e) {
                    Log.d("API REQUEST catch:", e.getMessage());
                }

                dialog.dismiss();

            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
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
        });

        AppController.getInstance().addToRequestQueue(request, TAG);
        return recipeList;
    }

    @Override
    public void onResume(){
        super.onResume();
        loadList();
    }

    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
