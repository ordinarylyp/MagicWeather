package com.lyp.magicweather.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    protected Unbinder mBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preOnCreateView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),container,false);
        mBinder = ButterKnife.bind(this,view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        mBinder.unbind();
        super.onDestroyView();
    }

    /**
     * 获取布局id
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化布局
     */
    public abstract void initView(View view);

    /**
     * 初始化数据
     */
    public abstract void initData();


    public void preOnCreateView(){

    }
}
