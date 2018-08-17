package com.lyp.magicweather.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @function SharedPreferences自定义功能类
 * @author lyp
 */
public class PreUtil {
    private static final String PREF_NAME = "config";

    /**
     * 读取字符串
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Context context, String key, String defaultValue){
        SharedPreferences sp=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sp.getString(key,defaultValue);
    }

    /**
     * 添加字符串
     * @param context
     * @param key
     * @param value
     */
    public static void setString(Context context, String key, String value){
        SharedPreferences sp=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key,value).apply();
    }

    /**
     * 读取boolean类型数据
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context context,String key, boolean defaultValue){
        SharedPreferences sp=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,defaultValue);
    }

    /**
     * 添加 boolean类型数据
     * @param context
     * @param key
     * @param value
     */
    public static void setBoolean(Context context, String key, boolean value){
        SharedPreferences sp= context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).apply();
    }

    /**
     * 读取int类型数据
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, String key, int defaultValue){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return  sp.getInt(key,defaultValue);
    }

    /**
     * 添加int类型数据
     * @param context
     * @param key
     * @param value
     */
    public static void setInt(Context context, String key, int value){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).apply();
    }

    /**
     * 清空数据
     * @param context
     */
    public static void clear(Context context){
        SharedPreferences sp=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
