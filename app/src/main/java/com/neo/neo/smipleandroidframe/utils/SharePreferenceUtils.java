package com.neo.neo.smipleandroidframe.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by neo on 15/5/28.
 */
public class SharePreferenceUtils {
    private static final String TOKEN = "token";
    private SharedPreferences mSharedPreferences;
    private Context context;

    public SharePreferenceUtils(Context context) {
        this.context = context;
    }

    public void saveToken(String token) {
        mSharedPreferences.edit().putString(TOKEN, token).commit();
    }

    public String getToken() {
        return mSharedPreferences.getString(TOKEN, null);
    }

}
