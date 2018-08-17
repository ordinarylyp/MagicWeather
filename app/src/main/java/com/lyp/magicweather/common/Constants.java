package com.lyp.magicweather.common;

/**
 * @function 常量
 * @author lyp
 */
public class Constants {
    //省、市、县
    public static final int LEVEL_PROVINCE=0;
    public static final int LEVEL_CITY=1;
    public static final int LEVEL_COUNTY=2;
    //获取地区Url
    public static final String URL_ADDRESS_BASE = "http://guolin.tech/api/china";
    //获取天气信息的基础Url
    public static final String URL_WEATHER_BASE = "https://free-api.heweather.com/s6/weather?location=";
    //天气认证key
    public static final String URL_WEATHER_KEY = "&key=d203150ffac340508f58ddb5acd119c8";
    //默认天气id
    public static final String DEFAULT_WEATHER_ID= "CN101010100";
    //天气id
    public static final String WEATHER_ID = "weather_id";
    //获取必应每日一图
    public static final String URL_BEING_PIC = "http://guolin.tech/api/bing_pic";
}
