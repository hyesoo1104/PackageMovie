package com.example.ohhye.packagemovie.singletone_object;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ohhye on 2015-03-26.
 */
public class Preferences {
    public static SharedPreferences mPref;

    private Preferences(Context mContext){
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public static SharedPreferences getPrefernces(){
        return mPref;
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public void putBoolean(String key, Boolean value){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getString(String key,String default_value){
        return mPref.getString(key,default_value);
    }

    public boolean getBoolean(String key, Boolean default_value){
        return mPref.getBoolean(key, default_value);
    }
}
