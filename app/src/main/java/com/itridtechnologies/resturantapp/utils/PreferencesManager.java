package com.itridtechnologies.resturantapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;



public class PreferencesManager {

    private final Context context;
    private static final String TAG = "PreferencesManager";
    private boolean checkLogin = true;

    public PreferencesManager(Context context) {
        this.context = context;
    }


    public void saveMyData(String key, String data) {
        //init
        SharedPreferences preferences = context.getSharedPreferences(
                "my_data",
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, data);
        editor.apply();

        Log.d(TAG, "saveMyData: saved> " + data);
    }//end fun


    public void saveMyDataBool(String key, Boolean data) {
        //init
        SharedPreferences preferences = context.getSharedPreferences(
                "my_data",
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, data);
        editor.apply();

        Log.d(TAG, "saveMyData: saved> " + data);
    }//end fun

    public String getMyDataString(String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                "my_data",
                Context.MODE_PRIVATE
        );
        return preferences.getString(key, "");
    }//end fun

    public boolean getMyDataBool(String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                "my_data",
                Context.MODE_PRIVATE
        );
        return preferences.getBoolean(key,false);
    }//end fun

    public void clearSharedPref() {

        SharedPreferences preferences = context.getSharedPreferences(
                "my_data",
                Context.MODE_PRIVATE
        );
        preferences.edit().clear().apply();

    }


}//end class
