package com.fmapp.test01.activity.login;


import android.os.Bundle;
import android.text.TextUtils;

import com.fmapp.test01.R;
import com.fmapp.test01.activity.main.MainActivity;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.utils.SharedPreferencesUtils;

public class SplashActivity extends BaseActivity {
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        token = SharedPreferencesUtils.getString(mContext, "token");
        if (TextUtils.isEmpty(token)) {
            toActivityFinish(LoginActivity.class);
        } else {
            toActivityFinish(MainActivity.class);
        }
    }
}
