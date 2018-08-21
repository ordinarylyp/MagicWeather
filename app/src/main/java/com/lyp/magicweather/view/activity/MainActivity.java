package com.lyp.magicweather.view.activity;

import android.content.Intent;

import com.lyp.magicweather.R;
import com.lyp.magicweather.base.BaseActivity;
import com.lyp.magicweather.util.PreUtil;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        String weatherString = PreUtil.getString(this,"weather",null);
        if (weatherString!=null){
            Intent intent =new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void initData() {

    }
}
