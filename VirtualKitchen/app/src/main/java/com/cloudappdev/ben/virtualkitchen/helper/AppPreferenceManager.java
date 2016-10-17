package com.cloudappdev.ben.virtualkitchen.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ben on 17/10/2016.
 */

public class AppPreferenceManager {
    public static String TAG = AppPreferenceManager.class.getSimpleName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "NCIGOAPP";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_REFRESH_RATE = "refreshRate";
    private static final String KEY_REG_ID = "regId";
    private static final String KEY_NOTIFICATION = "updateNotification";

    public AppPreferenceManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public void setString(String tag, String value){
        editor = pref.edit();
        editor.putString(tag, value);
        editor.apply();
    }

    public void setBoolean(String tag, boolean value){
        editor = pref.edit();
        editor.putBoolean(tag, value);
        editor.apply();
    }

    public void setInteger(String tag, int value){
        editor = pref.edit();
        editor.putInt(tag, value);
        editor.apply();
    }

    public void setLogin(boolean isLoggedIn){
        editor = pref.edit();
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getStringValue(String tag){
        return pref.getString(tag, "Null");
    }
    public boolean getBoolean(String tag){return pref.getBoolean(tag, false);}
    public int getInteger(String tag){return pref.getInt(tag, 0);}
}
