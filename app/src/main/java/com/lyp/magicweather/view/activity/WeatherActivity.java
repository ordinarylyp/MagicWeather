package com.lyp.magicweather.view.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.listener.AppBarStateChangeListener;
import com.liaoinstan.springview.widget.SpringView;
import com.lyp.magicweather.R;
import com.lyp.magicweather.base.BaseActivity;
import com.lyp.magicweather.common.Constants;
import com.lyp.magicweather.model.gson.Forecast;
import com.lyp.magicweather.model.gson.Suggestion;
import com.lyp.magicweather.model.gson.Weather;
import com.lyp.magicweather.service.AutoUpdateService;
import com.lyp.magicweather.util.HttpUtil;
import com.lyp.magicweather.util.PreUtil;
import com.lyp.magicweather.util.ToastUtil;
import com.lyp.magicweather.util.Utility;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {

    @BindView(R.id.nsv_layout)
    NestedScrollView nsvLayout;
    @BindView(R.id.tv_city)
    TextView tvCity;   //城市
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;    //更新时间
    @BindView(R.id.tv_now_temp)
    TextView tvNowTmp;     //温度
    @BindView(R.id.tv_now_info)
    TextView tvNowInfo;     //天气信息
    @BindView(R.id.tv_now_wind_dir)
    TextView tvNowWindDirection;   //风向
    @BindView(R.id.tv_now_wind_sc)
    TextView tvNowWindScale;    //风力
    @BindView(R.id.forecast_layout)
    LinearLayout forecastLayout;   //未来天气信息
    @BindView(R.id.tv_su_comfort)
    TextView tvSuComfort; // 舒适度
    @BindView(R.id.tv_su_comfort_info)
    TextView tvSuComfortInfo; //舒适度信息
    @BindView(R.id.tv_su_dress)
    TextView tvSuDress;    //穿着
    @BindView(R.id.tv_su_dress_info)
    TextView tvSuDressInfo;    //穿着信息
    @BindView(R.id.tv_su_flu)
    TextView tvSuFlu;     //感冒
    @BindView(R.id.tv_su_flu_info)
    TextView tvSuFluInfo; //感冒信息
    @BindView(R.id.tv_su_sport)
    TextView tvSuSport; //运动指数
    @BindView(R.id.tv_su_sport_info)
    TextView tvSuSportInfo; //运动指数信息
    @BindView(R.id.tv_su_travel)
    TextView tvSuTravel;    //旅行指数
    @BindView(R.id.tv_su_travel_info)
    TextView tvSuTravelInfo;  //旅行信息
    @BindView(R.id.tv_su_uv)
    TextView tvSuUv;  //紫外线指数
    @BindView(R.id.tv_su_uv_info)
    TextView tvSuUvInfo;  //紫外线信息
    @BindView(R.id.tv_su_car)
    TextView tvSuCar;     //洗车指数
    @BindView(R.id.tv_su_car_info)
    TextView tvSuCarInfo;   //洗车指数信息
    @BindView(R.id.tv_su_air)
    TextView tvSuAir;      //空气质量
    @BindView(R.id.tv_su_air_info)
    TextView tvSuAirInfo;  //空气质量信息
    @BindView(R.id.bing_image_view)
    ImageView bingImageView;   //必应图片
    @BindView(R.id.appBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;    //标题栏
    @BindView(R.id.spring_view)
    public SpringView springView;   //springview 刷新
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;

    private String mWeatherId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED){
                    //展开状态
                    springView.setEnable(true);
                }else if (state == State.COLLAPSED){
                    //折叠状态
                    springView.setEnable(false);
                }else {
                    springView.setEnable(false);
                }
            }
        });
    }

    @Override
    public void initData() {
        String weatherString = PreUtil.getString(this,"weather",null);
        if (weatherString!=null){
            //缓存中有数据时直接加载
            Weather weather = Utility.handleWeatherResponse(weatherString);
            try {
                mWeatherId = weather.basic.weatherId;
            }catch (NullPointerException e){
                mWeatherId = Constants.DEFAULT_WEATHER_ID;
                e.printStackTrace();
            }
            showWeatherInfo(weather);
        }else {
            //无缓存时向服务器查询天气
            mWeatherId = getIntent().getStringExtra(Constants.WEATHER_ID);
            nsvLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        String bingPic = PreUtil.getString(this,"bing_pic",null);
        if (bingPic!=null){
            Glide.with(this).load(bingPic).into(bingImageView);
        }else {
            loadBingPic();
        }
        initSpringView();
    }

    private void initSpringView(){
        final DefaultHeader header = new DefaultHeader(this);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }

            @Override
            public void onLoadmore() {

            }
        });
        springView.setHeader(header);
        springView.setEnable(false);
    }

    @OnClick(R.id.btn_menu)
    public void onClick(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void loadBingPic(){
        HttpUtil.sendOkHttpRequest(Constants.URL_BEING_PIC, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                PreUtil.setString(WeatherActivity.this,"bing_pic",bingPic);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingImageView);
                    }
                });
            }
        });
    }

    public void requestWeather(String weatherId){
        String weatherUrl = Constants.URL_WEATHER_BASE+weatherId+Constants.URL_WEATHER_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (springView!=null){
                            springView.onFinishFreshAndLoad();
                        }
                        ToastUtil.showShort("获取天气信息失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (springView!=null){
                            springView.onFinishFreshAndLoad();
                        }
                        if (weather!=null && "ok".equals(weather.status)){
                            PreUtil.setString(WeatherActivity.this,"weather",responseText);
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        }else {
                            ToastUtil.showShort("获取天气信息失败");
                        }
                    }
                });
            }
        });
    }


    private void showWeatherInfo(Weather weather){
        //头部显示basic和now信息
        String cityName = weather.basic.cityName;
        String updateTime = weather.update.locateTime.split(" ")[1];
        String temperature = weather.now.temperature+"℃";
        String weatherInfo = weather.now.info;
        String windDir = weather.now.windDirection;
        String windSC = weather.now.windScale+"级";
        tvCity.setText(cityName);
        tvUpdateTime.setText(updateTime);
        tvNowTmp.setText(temperature);
        tvNowInfo.setText(weatherInfo);
        tvNowWindDirection.setText(windDir);
        tvNowWindScale.setText(windSC);

        //未来天气信息
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView tvDate= ButterKnife.findById(view,R.id.tv_forecast_date);
            TextView tvDayInfo = ButterKnife.findById(view,R.id.tv_forecast_d_info);
            TextView tvNightInfo =  ButterKnife.findById(view,R.id.tv_forecast_n_info);
            TextView tvMax = ButterKnife.findById(view,R.id.tv_forecast_max);
            TextView tvMin = ButterKnife.findById(view,R.id.tv_forecast_min);
            tvDate.setText(forecast.date);
            tvDayInfo.setText("白天 "+forecast.dayInfo);
            tvNightInfo.setText("夜晚 "+forecast.nightInfo);
            tvMax.setText(forecast.tmpMax+"℃");
            tvMin.setText(forecast.tmpMin+"℃");
            forecastLayout.addView(view);

        }

        //生活建议
        for (Suggestion suggestion:weather.suggestionList){
            if ("comf".equals(suggestion.type)){
                String comfort = "舒适："+suggestion.brief;
                tvSuComfort.setText(comfort);
                tvSuComfortInfo.setText(suggestion.info);
            }else if ("drsg".equals(suggestion.type)){
                String dress = "衣着："+suggestion.brief;
                tvSuDress.setText(dress);
                tvSuDressInfo.setText(suggestion.info);
            }else if ("flu".equals(suggestion.type)){
                String flu = "感冒："+suggestion.brief;
                tvSuFlu.setText(flu);
                tvSuFluInfo.setText(suggestion.info);
            }else if ("sport".equals(suggestion.type)){
                String sport = "运动:"+suggestion.brief;
                tvSuSport.setText(sport);
                tvSuSportInfo.setText(suggestion.info);
            }else if ("trav".equals(suggestion.type)){
                String travel = "旅行："+suggestion.brief;
                tvSuTravel.setText(travel);
                tvSuTravelInfo.setText(suggestion.info);
            }else if ("uv".equals(suggestion.type)){
                String uv = "紫外线："+suggestion.brief;
                tvSuUv.setText(uv);
                tvSuUvInfo.setText(suggestion.info);
            }else if ("cw".equals(suggestion.type)){
                String carWash = "洗车："+suggestion.brief;
                tvSuCar.setText(carWash);
                tvSuCarInfo.setText(suggestion.info);
            }else if ("air".equals(suggestion.type)){
                String air = "空气质量："+suggestion.brief;
                tvSuAir.setText(air);
                tvSuAirInfo.setText(suggestion.info);
            }
        }
        nsvLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

}
