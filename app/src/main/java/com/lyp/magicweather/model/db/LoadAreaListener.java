package com.lyp.magicweather.model.db;

public interface LoadAreaListener {

    void onFinish(final String type, String responseText); //获取数据成功
    void onError();                                        //获取数据失败
}
