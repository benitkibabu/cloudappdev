package com.cloudappdev.ben.virtualkitchen.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudappdev.ben.virtualkitchen.R;

/**
 *created by Ben 23/10/2016
 *
 */
public class MyFavouriteFragment extends Fragment {

    public MyFavouriteFragment() {
        // Required empty public constructor
    }

    public static MyFavouriteFragment newInstance(String param1, Bundle param2) {
        MyFavouriteFragment fragment = new MyFavouriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favourite, container, false);

        return view;
    }

}
