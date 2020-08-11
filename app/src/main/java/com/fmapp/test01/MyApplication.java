package com.fmapp.test01;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arialyy.aria.core.Aria;
import com.fmapp.test01.utils.SharedPreferencesUtils;

import java.util.LinkedList;
import java.util.List;


/**
 * user：lqm
 * desc：
 */

public class MyApplication extends Application {
    private boolean serviceRun;

    private boolean isShowToast = false;

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
        serviceRun = false;
        Aria.init(this);
        Aria.get(this).getDownloadConfig().setMaxTaskNum(3);
        // startService(new Intent(this, KeepLifeService.class));
        //Hawk存储初始化
        //Hawk.init(this).build();
        // 不耗时，做一些简单初始化准备工作，不会启动下载进程
        //定义前台服务的默认样式。即标题、描述和图标
//        ForegroundNotification foregroundNotification = new ForegroundNotification("测试","描述", R.mipmap.ic_launcher,
//                //定义前台服务的通知点击事件
//                new ForegroundNotificationClickListener() {
//
//                    @Override
//                    public void foregroundNotificationClick(Context context, Intent intent) {
//                    }
//                });
//        //启动保活服务
//        KeepLive.startWork(this, KeepLive.RunMode.ENERGY, foregroundNotification,
//                //你需要保活的服务，如socket连接、定时任务等，建议不用匿名内部类的方式在这里写
//                new KeepLiveService() {
//                    /**
//                     * 运行中
//                     * 由于服务可能会多次自动启动，该方法可能重复调用
//                     */
//                    @Override
//                    public void onWorking() {
//
//                    }
//                    /**
//                     * 服务终止
//                     * 由于服务可能会被多次终止，该方法可能重复调用，需同onWorking配套使用，如注册和注销broadcast
//                     */
//                    @Override
//                    public void onStop() {
//
//                    }
//                }
//        );
        initBackgroundCallBack();

    }

    int account = 0;
    boolean isRunInBackground = true;

    private void initBackgroundCallBack() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                account++;
                if (isRunInBackground) {
                    // 后台到前台，在此进行相应操作
                    isRunInBackground = false;
                    Toast.makeText(mContext, "推至前台了", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                account--;
                if (account == 0) {
                    // 前台到后台，在此进行相应操作
                    isRunInBackground = true;
                    Toast.makeText(mContext, "推至后台了", Toast.LENGTH_SHORT).show();
                    Log.d("推至后台了", "");
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }


    public static MyApplication getInstance(Context mContext) {
        return instance;
    }

    public static String getToken(Context mContext) {
        String token = SharedPreferencesUtils.getString(mContext, "token");
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