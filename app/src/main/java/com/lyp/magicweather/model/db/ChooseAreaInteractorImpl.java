package com.lyp.magicweather.model.db;

import com.lyp.magicweather.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaInteractorImpl implements ChooseAreaInteractor  {

    /**
     * 向服务器请求数据
     * @param url
     * @param type
     * @param listener
     */
    @Override
    public void loadArea(String url, final String type, final LoadAreaListener listener) {
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                listener.onFinish(type,responseText);
            }
        });
    }
}
