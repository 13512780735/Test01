package com.feemoo.activity.MyInfo;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.feemoo.R;
import com.feemoo.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;

public class MessageActivity extends BaseActivity {
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
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
}
