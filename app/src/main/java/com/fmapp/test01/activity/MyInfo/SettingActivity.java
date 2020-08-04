package com.fmapp.test01.activity.MyInfo;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.fmapp.test01.R;
import com.fmapp.test01.activity.login.LoginActivity;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.utils.AppManager;
import com.fmapp.test01.utils.CustomDialog;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.fmapp.test01.utils.StatusBarUtil;

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
        mBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back01));
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
