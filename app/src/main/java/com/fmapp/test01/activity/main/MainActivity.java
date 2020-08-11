package com.fmapp.test01.activity.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import com.fmapp.test01.R;
import com.fmapp.test01.fragment.main.CloudFragment;
import com.fmapp.test01.fragment.main.MyFragment;
import com.fmapp.test01.fragment.main.SelectFragment;
import com.fmapp.test01.utils.AppManager;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.fmapp.test01.utils.StatusBarUtil;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_radio)
    RadioGroup mRadioGroup;

    SelectFragment selectFragment;
    CloudFragment cloudFragment;
    MyFragment myFragment;
    FragmentTransaction mTransaction;
    FragmentManager mSupportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int color = getResources().getColor(R.color.gray_background_color);
        StatusBarUtil.setColor(this, color, 0);
        StatusBarUtil.setLightMode(this);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initFragment();
    }

    private void initFragment() {
        Log.d("token", SharedPreferencesUtils.getString(this, "token"));
        mSupportFragmentManager = getSupportFragmentManager();
        mTransaction = mSupportFragmentManager.beginTransaction();
        selectFragment = SelectFragment.newInstance();
        cloudFragment = CloudFragment.newInstance();
        myFragment = MyFragment.newInstance();
        //初始化展示HomePageFragment
        mTransaction.add(R.id.main_frag, selectFragment)
                .add(R.id.main_frag, cloudFragment)
                .add(R.id.main_frag, myFragment);
        mTransaction.show(selectFragment).hide(cloudFragment).hide(myFragment);
        mTransaction.commit();
        mRadioGroup.check(mRadioGroup.getChildAt(0).getId());
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                FragmentTransaction transaction2 = mSupportFragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.radioButton:
                        transaction2.show(selectFragment).hide(cloudFragment).hide(myFragment).commit();
                        break;
                    case R.id.radioButton2:
                        transaction2.show(cloudFragment).hide(selectFragment).hide(myFragment).commit();
                        break;
                    case R.id.radioButton3:
                        transaction2.show(myFragment).hide(cloudFragment).hide(selectFragment).commit();
                        break;
                }
            }
        });
    }

    public boolean isForeground = false;

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (isForeground == false) {
//            T.show(MainActivity.this, "运行", 1);
//            isForeground = true;
//        }
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (!isAppOnForeground()) {
//            isForeground = false;
//            T.show(MainActivity.this, "暂停", 1);
//            Log.d("已经退出","1111");
//        }
//    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
/**
 * 获取Android设备中所有正在运行的App
 */
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
// The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
