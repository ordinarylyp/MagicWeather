package com.lyp.magicweather.presenter;

import android.app.Activity;
import android.app.Fragment;

import com.lyp.magicweather.model.db.ChooseAreaInteractor;
import com.lyp.magicweather.model.db.ChooseAreaInteractorImpl;
import com.lyp.magicweather.model.db.City;
import com.lyp.magicweather.model.db.LoadAreaListener;
import com.lyp.magicweather.model.db.Province;
import com.lyp.magicweather.util.Utility;
import com.lyp.magicweather.view.fragment.ChooseAreaView;

public class ChooseAreaPresenterImpl implements ChooseAreaPresenter,LoadAreaListener {

   private ChooseAreaView mChooseAreaView;
   private ChooseAreaInteractor mInteractor;
   private Activity mActivity;
    //选中的省份
    public Province selectedProvince;
    //选中的城市
    public City selectedCity;


    public ChooseAreaPresenterImpl(ChooseAreaView mChooseAreaView, Activity mActivity) {
        this.mChooseAreaView = mChooseAreaView;
        this.mActivity = mActivity;
        mInteractor = new ChooseAreaInteractorImpl();
    }

    @Override
    public void queryFromServer(String url, String type) {
        mChooseAreaView.showProgressDialog();
        mInteractor.loadArea(url,type,this);
    }

    @Override
    public void onDestroy() {
        mChooseAreaView = null;
    }

    @Override
    public void onFinish(final String type, String responseText) {
        boolean result = false;
        if ("province".equals(type)){
            result = Utility.handleProvinceResponse(responseText);
        }else if ("city".equals(type)){
            result = Utility.handleCityResponse(responseText,selectedProvince.getId());
        }else if ("county".equals(type)){
            result = Utility.handleCountyResponse(responseText,selectedCity.getId());
        }
        if (result){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mChooseAreaView.closeProgressDialog();
                    if ("province".equals(type)){
                        mChooseAreaView.queryProvinces();
                    }else if ("city".equals(type)){
                        mChooseAreaView.queryCities();
                    }else if ("county".equals(type)){
                        mChooseAreaView.queryCounties();
                    }
                }
            });

        }
    }

    @Override
    public void onError() {
        mChooseAreaView.closeProgressDialog();
        mChooseAreaView.showMsg("加载数据失败");
    }
}
