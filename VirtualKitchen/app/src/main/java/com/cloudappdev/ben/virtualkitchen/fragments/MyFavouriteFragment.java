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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "User";

    private String mParam1;
    private Bundle mParam2;

    public MyFavouriteFragment() {
        // Required empty public constructor
    }

    public static MyFavouriteFragment newInstance(String param1, Bundle param2) {
        MyFavouriteFragment fragment = new MyFavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBundle(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getBundle(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favourite, container, false);

        return view;
    }

}
