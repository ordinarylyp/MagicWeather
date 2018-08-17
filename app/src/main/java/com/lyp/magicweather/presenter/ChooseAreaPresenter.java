package com.lyp.magicweather.presenter;

public interface ChooseAreaPresenter {
  void queryFromServer(String url,String type);
  void onDestroy();

}
