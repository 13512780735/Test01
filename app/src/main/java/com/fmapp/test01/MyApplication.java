package com.fmapp.test01;

import android.app.Activity;
import android.app.Application;
import android.content.Context;


import com.fmapp.test01.utils.SharedPreferencesUtils;

import java.util.LinkedList;
import java.util.List;


/**
 * user：lqm
 * desc：
 */

public class MyApplication extends Application {
    private boolean serviceRun;

    private boolean isShowToast=false;

    public boolean isShowToast() {
        return isShowToast;
    }

    public void setShowToast(boolean showToast) {
        isShowToast = showToast;
    }



    public static List<Activity> activities = new LinkedList<>();

    public static MyApplication mContext;
    private static MyApplication instance;
    public static MyApplication getInstance() {
        if (mContext == null) {
            return new MyApplication();
        } else {
            return mContext;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        getToken(mContext);
        serviceRun=false;
    }


    public static MyApplication getInstance(Context mContext) {
        return instance;
    }
    public static String getToken(Context mContext) {
        String token= SharedPreferencesUtils.getString(mContext,"token");
        return token;
    }


    /**
     * 配置AutoLayout
     */
    private void initAutoLayout() {
        //默认使用的高度是设备的可用高度，也就是不包括状态栏和底部的操作栏的，以下配置可以拿到设备的物理高度进行百分比
//        AutoLayoutConifg.getInstance().useDeviceSize();
    }


    public static Context getmContext() {
        return mContext;
    }

    /**
     * 退出程序
     */
    public static void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

}