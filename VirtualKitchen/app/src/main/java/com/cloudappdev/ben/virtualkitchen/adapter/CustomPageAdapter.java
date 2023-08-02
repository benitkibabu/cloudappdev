package com.cloudappdev.ben.virtualkitchen.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Ben on 19/10/2016.
 */

public class CustomPageAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    List<String> titles;

    public CustomPageAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String>  titles){
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }
    public CustomPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return titles.get(position);
    }
}
