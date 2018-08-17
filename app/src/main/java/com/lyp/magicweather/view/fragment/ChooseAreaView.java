package com.lyp.magicweather.view.fragment;

public interface ChooseAreaView {

    void showProgressDialog();
    void closeProgressDialog();
    void queryProvinces();
    void queryCities();
    void queryCounties();
    void showMsg(String msg);
}
