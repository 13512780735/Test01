package com.feemoo.activity.file;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.feemoo.R;
import com.feemoo.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;

import butterknife.BindView;

public class PictureActivity extends BaseActivity {
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.ivPic)
    ImageView ivPic;
    private String uri;
    private String name;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        uri = getIntent().getStringExtra("uri");
        name = getIntent().getStringExtra("name");
        flag = getIntent().getStringExtra("flag");//0是本地 1是网络
        mTitle.setText(name);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // ImageLoaderUtils.getInstance(mContext).displayImage(uri, ivPic);
        if ("0".equals(flag)) {
            File file = new File(uri);
            Glide.with(this).load(file).into(ivPic);
        } else {
            Glide.with(this).load(uri).into(ivPic);
        }
    }
}
