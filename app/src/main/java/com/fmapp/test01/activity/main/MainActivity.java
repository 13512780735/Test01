package com.fmapp.test01.activity.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fmapp.test01.MyApplication;
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

    private final int REQUEST_PERMISSION_CODE = 1;//请求码
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

        Display display = getWindowManager().getDefaultDisplay();
        int width=display.getWidth();
        int height=display.getHeight();
        SharedPreferencesUtils.put(this,"width",width);
        SharedPreferencesUtils.put(this,"height",height);
        initFragment();
        checkPermission();
    }

    private void checkPermission() {
        //判断是否6.0以上的手机
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //PackageManager.PERMISSION_GRANTED表示同意授权
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //用户已经拒绝过且选择了不再提示，要给用户开通权限的提示
                //ActivityCompat.shouldShowRequestPermissionRationale返回false是用户选择了不再提示
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "请允许文件读写权限，否则无法正常使用本应用", Toast.LENGTH_LONG).show();
                }

                //申请权限
                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE);//(activity,权限数组,请求码)
            }
        }
    }
    /**
     * 获取权限请求结果
     *
     * @param requestCode  请求码，即REQUEST_PERMISSION_CODE
     * @param permissions  权限列表，即permissions数组
     * @param grantResults 申请结果，0为成功，-1为失败
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == 0) {
                    Log.i("MainActivity", permissions[i] + "申请成功");
                } else {
                    Log.i("MainActivity", permissions[i] + "申请失败");
                }
            }
        }
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
