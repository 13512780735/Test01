package com.fmapp.test01.activity.file;


import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.SuperKotlin.pictureviewer.ImagePagerActivity;
import com.SuperKotlin.pictureviewer.PictureConfig;
import com.bumptech.glide.Glide;
import com.fmapp.test01.R;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.utils.ImageLoaderUtil;
import com.fmapp.test01.utils.ImageLoaderUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PictureActivity extends BaseActivity {
    @BindView(R.id.tv_back)
    ImageView mBack;
    @BindView(R.id.ivPic)
    ImageView ivPic;
    private String uri;
    private String name;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        //uri = getIntent().getStringExtra("uri");
        uri = "/storage/emulated/0/Download/上传下载/上传文件icon@2x.png";
        name = getIntent().getStringExtra("name");
        flag = getIntent().getStringExtra("flag");//0是本地 1是网络
        Log.d("路徑：",uri);
        setBackView();
        mBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        setTitle(name);
        // ImageLoaderUtils.getInstance(mContext).displayImage(uri, ivPic);
        if ("0".equals(flag)) {
            File file = new File(uri);
            Glide.with(this).load(file).into(ivPic);
        }else {
            Glide.with(this).load(uri).into(ivPic);
        }
    }
}
