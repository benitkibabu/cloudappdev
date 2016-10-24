package com.cloudappdev.ben.virtualkitchen.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudappdev.ben.virtualkitchen.R;


public class RecipeList extends Fragment {

    private final String API_ID = getResources().getString(R.string.edamam_api_id);
    private final String API_KEY = getResources().getString(R.string.edamam_api_key);
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

   // private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public RecipeList() {
    }

    public static RecipeList newInstance(String param1, String param2) {
        RecipeList fragment = new RecipeList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    private void getRecipes(){

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
