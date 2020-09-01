package com.feemoo.activity;


import android.view.View;

import com.feemoo.R;
import com.feemoo.base.BaseActivity;

public class ScanActivity extends BaseActivity {

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                // onBackPressed();
                break;
        }
    }

    public void initUI() {
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
    }
}
