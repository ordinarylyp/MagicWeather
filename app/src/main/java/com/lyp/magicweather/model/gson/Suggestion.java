package com.lyp.magicweather.model.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {


    public String type;

    @SerializedName("brf")
    public String brief;

    @SerializedName("txt")
    public String info;

}
