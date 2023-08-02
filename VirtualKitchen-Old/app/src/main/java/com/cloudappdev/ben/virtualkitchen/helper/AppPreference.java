package com.cloudappdev.ben.virtualkitchen.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Benit Kibabu on 26/05/2017.
 */

public class AppPreference {

    SharedPreferences pref;
    Editor editor;
    Context _context;

    private final int PRIVATE_MODE = 0;
    private final String PREF_NAME = "vk_pref";
    public  final String isLoggedIn = "isLoggedIn";

    public AppPreference(Context context){
        this._context = context;
        pref = _context.getSharedPreferences( PREF_NAME, PRIVATE_MODE);
    }

    public void setString(String key, String val){
        editor = pref.edit();
        editor.putString(key, val);
        editor.apply();
    }
    public void setBoolean(String key, boolean val){
        editor = pref.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }


    public String getStringVal(String key){
        return pref.getString(key, "");
    }
    public boolean getBooleanVal(String key){
        return pref.getBoolean(key, false);
    }

}
