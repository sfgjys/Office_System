package com.minlu.baselibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    // 注意：在获取SharedPreferences对象时，如果getSharedPreferences方法的参数一是已经存在的文件时，那就不重新创建该文件，而是打开该文件存储取出数据

    private static SharedPreferences sharedPreferences;

    // 存储布尔值
    public static boolean saveBoolean(Context context, String key, boolean value) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaults) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaults);
    }

    // 存int值
    public static boolean saveInt(Context context, String key, int value) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defaults) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaults);
    }

    // 存String值
    public static boolean saveString(Context context, String key, String value) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defaults) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaults);
    }


    // 存Long值
    public static boolean saveLong(Context context, String key, long value) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key, long defaults) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaults);
    }
}
