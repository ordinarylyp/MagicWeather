package com.lyp.magicweather.util;

import android.content.Context;
import android.widget.Toast;

import com.lyp.magicweather.MyApplication;


/**
 * function:简单封装 Toast
 * @author lyp
 */
public class ToastUtil {
    private static Toast toast;
    private static Context context= MyApplication.getContext();

    /**
     * 短时间显示
     * @param text 显示文本
     */
    public static void showShort(String text){
        if (toast==null){
            toast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }
        else {
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 长时间显示
     * @param text
     */
    public static void showLong(String text){
        if (toast==null){
            toast=Toast.makeText(context,text,Toast.LENGTH_LONG);
        }else {
            toast.setText(text);
        }
        toast.show();
    }
}
