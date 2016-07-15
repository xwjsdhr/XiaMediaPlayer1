package com.xwj.xiamediaplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xwjsd on 2016-04-29.
 */
public class PreferenceUtils {
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constant.APP_SETTINGS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences(Constant.APP_SETTINGS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static void putInt(Context context, String key, int value) {
        context.getSharedPreferences(Constant.APP_SETTINGS, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public static int getInt(Context context, String key) {
        return context.getSharedPreferences(Constant.APP_SETTINGS, Context.MODE_PRIVATE)
                .getInt(key, -1);
    }
}
