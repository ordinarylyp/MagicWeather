package com.lyp.magicweather.util;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author lyp
 * 网络请求工具类
 * */
public class HttpUtil {

    /**
     * 用okHttp发送网络请求
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        Call call =client.newCall(request);
        call.enqueue(callback);
    }
}
