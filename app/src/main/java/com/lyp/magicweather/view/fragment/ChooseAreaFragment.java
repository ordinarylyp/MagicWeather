package com.lyp.magicweather.view.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lyp.magicweather.MyApplication;
import com.lyp.magicweather.R;
import com.lyp.magicweather.base.BaseFragment;
import com.lyp.magicweather.common.Constants;
import com.lyp.magicweather.model.db.City;
import com.lyp.magicweather.model.db.County;
import com.lyp.magicweather.model.db.Province;
import com.lyp.magicweather.presenter.ChooseAreaPresenterImpl;
import com.lyp.magicweather.util.ToastUtil;
import com.lyp.magicweather.view.activity.MainActivity;
import com.lyp.magicweather.view.activity.WeatherActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChooseAreaFragment extends BaseFragment implements ChooseAreaView {

    @BindView(R.id.tv_title)
    TextView tvTitle;   //标题
    @BindView(R.id.btn_back)
    Button btnBack;    //返回按钮
    @BindView(R.id.ll_locate_layout)
    LinearLayout llLocateLayout;  //定位整体布局
    @BindView(R.id.layout_locate)
    LinearLayout layoutLocate; //定位信息
    @BindView(R.id.tv_located_city)
    TextView tvLocatedCity;  //位置
    @BindView(R.id.lv_select_area)
    ListView lvSelectArea;

    private ProgressDialog progressDialog;  //信息弹窗

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList; //省列表

    private List<City> cityList;   //市列表

    private List<County> countyList;   //县列表

    private Province selectedProvince;   //选中的省

    private City selectedCity;    //选中的市

    private int currentLevel;     //当前选中的等级

    public LocationClient mLocationClient;

    private String mCity;

    private ChooseAreaPresenterImpl mPresenter;

    @Override
    public void preOnCreateView() {
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
    }

    @Override
    public int getLayoutId() {
        return R.layout.choose_area;
    }

    @Override
    public void initView(View view) {
        adapter = new ArrayAdapter<String>(MyApplication.getContext(),android.R.layout.simple_list_item_1,dataList);
        lvSelectArea.setAdapter(adapter);
        mPresenter = new ChooseAreaPresenterImpl(this,getActivity());
        // 声明权限，将权限添加到list集合中再一次性申请
        List<String> permissionList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        }else {
            requestLocation();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvSelectArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == Constants.LEVEL_PROVINCE){
                    selectedProvince = mPresenter.selectedProvince = provinceList.get(position);
                    queryCities();
                }else if (currentLevel == Constants.LEVEL_CITY){
                    selectedCity = mPresenter.selectedCity = cityList.get(position);
                    queryCounties();
                }else if(currentLevel == Constants.LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity){
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra(Constants.WEATHER_ID,weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.springView.setEnable(false);
                        activity.requestWeather(weatherId);
                    }
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == Constants.LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel == Constants.LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        layoutLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity){
                    Intent intent = new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra(Constants.WEATHER_ID,mCity);
                    startActivity(intent);
                    getActivity().finish();
                }else if (getActivity() instanceof WeatherActivity){
                    WeatherActivity activity = (WeatherActivity) getActivity();
                    activity.drawerLayout.closeDrawers();
                    activity.springView.setEnable(false);
                    activity.requestWeather(mCity);
                }
            }
        });
        queryProvinces();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 ){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            ToastUtil.showShort("必须同意所有权限才能使用本程序");
                            getActivity().finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    ToastUtil.showShort("发生未知错误");
                    getActivity().finish();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 请求位置服务
     */
    private void requestLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(500);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 显示各个省份的数据
     */
    @Override
    public void queryProvinces() {
        tvTitle.setText("中国");
        btnBack.setVisibility(View.GONE);
        llLocateLayout.setVisibility(View.VISIBLE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size()>0){
            dataList.clear();
            for (Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            lvSelectArea.setSelection(0);
            currentLevel = Constants.LEVEL_PROVINCE;
        }else {
            String url = Constants.URL_ADDRESS_BASE;
            mPresenter.queryFromServer(url,"province");
        }
    }

    @Override
    public void queryCities() {
        tvTitle.setText(selectedProvince.getProvinceName());
        btnBack.setVisibility(View.VISIBLE);
        llLocateLayout.setVisibility(View.GONE);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size()>0){
            dataList.clear();
            for (City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            lvSelectArea.setSelection(0);
            currentLevel = Constants.LEVEL_CITY;
        }else {
            String url =Constants.URL_ADDRESS_BASE+"/"+selectedProvince.getProvinceCode();
            mPresenter.queryFromServer(url,"city");
        }
    }

    @Override
    public void queryCounties() {
        tvTitle.setText(selectedCity.getCityName());
        btnBack.setVisibility(View.VISIBLE);
        llLocateLayout.setVisibility(View.GONE);
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size()>0){
            dataList.clear();
            for (County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            lvSelectArea.setSelection(0);
            currentLevel = Constants.LEVEL_COUNTY;
        }else {
            String url = Constants.URL_ADDRESS_BASE+"/"+selectedProvince.getProvinceCode()+"/"+selectedCity.getCityCode();
            mPresenter.queryFromServer(url,"county");
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mCity = bdLocation.getCity();
            Log.e("ChooseAreaFragment:",mCity+"");
            tvLocatedCity.setText(mCity);
        }
    }

    @Override
    public void showMsg(String msg) {
        ToastUtil.showShort(msg);
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }
}
