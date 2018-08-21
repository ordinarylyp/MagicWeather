package com.lyp.magicweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.lyp.magicweather.common.Constants;
import com.lyp.magicweather.model.gson.Weather;
import com.lyp.magicweather.util.HttpUtil;
import com.lyp.magicweather.util.PreUtil;
import com.lyp.magicweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        //Alarm机制，实现定时更新
        AlarmManager manager =(AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60*60*1000; // 一个小时
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi =PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气
     */
    private void updateWeather(){
        String weatherString = PreUtil.getString(this,"weather",null);
        if (weatherString !=null ){
            //有缓存直接解析数据
            final Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;

            String weatherUrl = Constants.URL_WEATHER_BASE+weatherId+Constants.URL_WEATHER_KEY;
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather1 = Utility.handleWeatherResponse(responseText);
                    if (weather1!=null && "ok".equals(weather1.status)){
                        PreUtil.setString(AutoUpdateService.this,"weather",responseText);
                    }
                }
            });
        }
    }

    private void updateBingPic(){
        String requestBingPic = Constants.URL_BEING_PIC;
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                PreUtil.setString(AutoUpdateService.this,"bing_pic",bingPic);
            }
        });
    }

}
