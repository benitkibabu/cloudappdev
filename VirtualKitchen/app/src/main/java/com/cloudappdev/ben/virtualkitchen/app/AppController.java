package com.cloudappdev.ben.virtualkitchen.app;

import android.app.Application;

import com.cloudappdev.ben.virtualkitchen.R;

/**
 * Created by Ben on 17/10/2016.
 */
public class AppController extends Application {

    public String searchKey = "chicken";

    private static AppController mInstance;
    String navFragment;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public String getNavFragment() {
        return navFragment;
    }

    public void setNavFragment(String navFragment) {
        this.navFragment = navFragment;
    }

    public String appKey(){
        return getString(R.string.vk_app_key);
    }
}
