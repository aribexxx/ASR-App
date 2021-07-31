package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地数据存储与读取（共享参数）
 */
public class SharePreferenceUtil {

    public static boolean saveBoolean(Context context, String fileName, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        Map<String, Boolean> map = new HashMap<>();
        map.put(key, value);
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
}

