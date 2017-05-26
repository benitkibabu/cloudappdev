package com.cloudappdev.ben.virtualkitchen.app;

import android.app.Application;

import com.cloudappdev.ben.virtualkitchen.R;
import com.cloudappdev.ben.virtualkitchen.models.User;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by Ben on 17/10/2016.
 */
public class AppController extends Application {

    public String searchKey = "chicken";

    private static AppController mInstance;
    User user;
    String navFragement;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNavFragement() {
        return navFragement;
    }

    public void setNavFragement(String navFragement) {
        this.navFragement = navFragement;
    }

    public String appKey(){
        return getString(R.string.vk_app_key);
    }
}
