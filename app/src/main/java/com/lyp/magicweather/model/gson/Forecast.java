package com.lyp.magicweather.model.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    public String date; //时间

    @SerializedName("cond_txt_d")
    public String dayInfo;

    @SerializedName("cond_txt_n")
    public String nightInfo;

    @SerializedName("tmp_max")
    public String tmpMax;

    @SerializedName("tmp_min")
    public String tmpMin;

}
