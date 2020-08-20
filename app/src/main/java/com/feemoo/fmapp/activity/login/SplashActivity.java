package com.feemoo.fmapp.activity.login;


import android.os.Bundle;
import android.text.TextUtils;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.main.MainActivity;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;

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
