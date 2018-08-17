package com.lyp.magicweather.model.gson;

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("tmp")
    public String temperature; //温度

    @SerializedName("cond_txt")
    public String info; //天气状况

    @SerializedName("wind_dir")
    public String windDirection; //风向

    @SerializedName("wind_sc")
    public String windScale;  //风力等级

}
