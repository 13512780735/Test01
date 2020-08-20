package com.feemoo.fmapp.activity.MyInfo;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.login.LoginActivity;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.utils.AppManager;
import com.feemoo.fmapp.utils.CustomDialog;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.feemoo.fmapp.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_back)
    ImageView mBack;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        int color = getResources().getColor(R.color.white);
        StatusBarUtil.setColor(this, color, 0);
        StatusBarUtil.setLightMode(this);
        initUI();
    }

    private void initUI() {
        setBackView();
        mBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        setTitle("设置");
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
