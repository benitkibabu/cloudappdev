package com.cloudappdev.ben.virtualkitchen.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.cloudappdev.ben.virtualkitchen.rest.APIService;
import com.cloudappdev.ben.virtualkitchen.rest.RetrofitClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Ben on 17/10/2016.
 */

public class AppConfig {

    public static final String TEST_INTERNAL_API_URL = "http://127.0.0.1:3000/api/";
    public static final String BASE_INTERNAL_API_URL = "https://virtualk.herokuapp.com/api/";
    public static APIService getAPIService(){
        return RetrofitClient.getClient(BASE_INTERNAL_API_URL).create(APIService.class);
    }

    public static APIService getAPIService(String externalAPI){
        return RetrofitClient.getClient(externalAPI).create(APIService.class);
    }


    public static final String SENDER_ID = "1059451623513";
    public static final String EXTRA_MESSAGE = "message";
    public static final String DISPLAY_MSG_ACTION = "DISPLAY_MESSAGE";

    public static final String TESCO_API = "http://itrackerapp.gear.host/ncigo/";
    public static final String UPCDB = "http://api.upcdatabase.org/json/";
    //http://api.upcdatabase.org/json/APIKEY/CODE
    public static final String UPLOOKUP = "https://api.upcitemdb.com/prod/trial/";
    //http://api.upcdatabase.org/json/APIKEY/CODE
    public static final String RECIPE_API = "https://api.edamam.com/";
    public static final String BREWERY_API = "";

    private static DateFormat dateTimeFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public static String getDateValue(Date date){
        return dateFormat.format(date);
    }

    public static String getTimeValue(Date date){
        return timeFormat.format(date);
    }

    public static String getDateTime() {
        Date date = new Date();
        return dateTimeFormat.format(date);
    }

    public static String generateID(){
        return UUID.randomUUID().toString();
    }

    public static Date getDate(String date) throws ParseException {
        return dateTimeFormat.parse(date);
    }

    public static String getMobileIMEI(Context c){
        TelephonyManager telephonyManager = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getVersion(Context context){
        PackageInfo pInfo;
        String version = "Null";
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    static void displayMessage(Context context, String message){
        Intent i = new Intent(DISPLAY_MSG_ACTION);
        i.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(i);
    }
}
