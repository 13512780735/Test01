package com.feemoo.activity.MyInfo;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.feemoo.R;
import com.feemoo.activity.login.LoginActivity;
import com.feemoo.base.BaseActivity;
import com.feemoo.utils.AppManager;
import com.feemoo.utils.CustomDialog;
import com.feemoo.utils.SharedPreferencesUtils;
import com.feemoo.utils.StatusBarUtil;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initUI();
    }

    private void initUI() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.tvLogout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogout:
                dialog = new CustomDialog(this).builder()
                        .setGravity(Gravity.CENTER)
                        .setTitle("提示", getResources().getColor(R.color.black))//可以不设置标题颜色，默认系统颜色
                        .setSubTitle("您是否要退出该账号")
                        .setNegativeButton("取消", R.color.button_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", R.color.button_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferencesUtils.put(mContext, "token", "");
                                toActivityFinish(LoginActivity.class);
                                AppManager.getAppManager().finishAllActivity();
                            }
                        })
                ;

                dialog.show();
                break;
        }
    }
}
