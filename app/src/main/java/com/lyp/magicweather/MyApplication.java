package com.lyp.magicweather;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import org.litepal.LitePal;

public class MyApplication extends Application{
    private static Context context;
    private static Handler mainHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        mainHandler=new Handler();
        // 调用 LitePal 的初始化方法
        LitePal.initialize(this);
    }

    public static Context getContext() {
        return context;
    }
}
