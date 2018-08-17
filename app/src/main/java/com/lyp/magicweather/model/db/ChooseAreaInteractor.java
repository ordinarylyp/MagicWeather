package com.lyp.magicweather.model.db;

public interface ChooseAreaInteractor {
    void loadArea(String url,final String type,LoadAreaListener listener);
}
