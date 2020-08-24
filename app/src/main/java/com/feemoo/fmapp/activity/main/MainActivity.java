package com.feemoo.fmapp.activity.main;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.fragment.main.CloudFragment;
import com.feemoo.fmapp.fragment.main.MyFragment;
import com.feemoo.fmapp.fragment.main.SelectFragment;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.gyf.immersionbar.ImmersionBar;


/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final int REQUEST_PERMISSION_CODE = 1;//请求码
    FrameLayout content;
    LinearLayout ll_select;
    LinearLayout ll_cloud;
    LinearLayout ll_my;
    SelectFragment selectFragment;
    CloudFragment cloudFragment;
    MyFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ButterKnife.bind(this);
        // AppManager.getAppManager().addActivity(this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        // ImmersionBar.with(this).statusBarDarkFont(true).navigationBarColor(R.color.gray_background_color).init();
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        SharedPreferencesUtils.put(this, "width", width);
        SharedPreferencesUtils.put(this, "height", height);
        initUI();
        checkPermission();
    }

    private void initUI() {
        content = findView(R.id.content);
        ll_select = findView(R.id.ll_select);
        ll_cloud = findView(R.id.ll_cloud);
        ll_my = findView(R.id.ll_my);
        ll_select.setOnClickListener(this);
        ll_cloud.setOnClickListener(this);
        ll_my.setOnClickListener(this);
        selectedFragment(0);
        tabSelected(ll_select);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_select:
                selectedFragment(0);
                tabSelected(ll_select);
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                break;
            case R.id.ll_cloud:
                selectedFragment(1);
                tabSelected(ll_cloud);
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                break;
            case R.id.ll_my:
                selectedFragment(2);
                tabSelected(ll_my);
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                break;
            default:
                break;
        }
    }

    private void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (selectFragment == null) {
                    selectFragment = new SelectFragment();
                    transaction.add(R.id.content, selectFragment);
                } else {
                    transaction.show(selectFragment);
                }
                break;
            case 1:
                if (cloudFragment == null) {
                    cloudFragment = new CloudFragment();
                    transaction.add(R.id.content, cloudFragment);
                } else {
                    transaction.show(cloudFragment);
                }
                break;
            case 2:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.content, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (selectFragment != null) {
            transaction.hide(selectFragment);
        }
        if (cloudFragment != null) {
            transaction.hide(cloudFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    private void tabSelected(LinearLayout linearLayout) {
        ll_select.setSelected(false);
        ll_cloud.setSelected(false);
        ll_my.setSelected(false);
        linearLayout.setSelected(true);
    }

}
