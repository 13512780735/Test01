package com.feemoo.fmapp.activity.MyInfo;


import android.os.Bundle;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.base.BaseActivity;

public class MessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initUI();
    }

    private void initUI() {
        setTitle("消息");
        setBackView();
    }
}
