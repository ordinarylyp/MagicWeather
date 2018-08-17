package com.lyp.magicweather.model.db;

public interface LoadAreaListener {

    void onFinish(final String type, String responseText);
    void onError();
}
