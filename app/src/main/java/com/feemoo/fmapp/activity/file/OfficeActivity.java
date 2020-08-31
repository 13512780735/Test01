package com.feemoo.fmapp.activity.file;


import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

import butterknife.BindView;

public class OfficeActivity extends BaseActivity implements TbsReaderView.ReaderCallback {
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.tv_title)
    TextView mTitle;
    RelativeLayout mRelativeLayout;
    private String path;
    private String name;
    private TbsReaderView mTbsReaderView;
    private String download = "/storage/emulated/0/Download/";
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        path = getIntent().getStringExtra("uri");
        name = getIntent().getStringExtra("name");
        mTitle.setText(name);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mTbsReaderView = new TbsReaderView(this, this);
        mRelativeLayout = findView(R.id.tbsView);
        mRelativeLayout.addView(mTbsReaderView, new RelativeLayout.LayoutParams(-1, -1));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(path));
//        } else {
//            uri = Uri.fromFile(new File(path));
//        }

        Log.d("路径", uri + "");
        File docFile = new File(download, name);
        displayFile(docFile.toString(), name);
    }


    private String tbsReaderTemp = Environment.getExternalStorageDirectory() + "/TbsReaderTemp";

    private void displayFile(String filePath, String fileName) {

        //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
        String bsReaderTemp = tbsReaderTemp;
        File bsReaderTempFile = new File(bsReaderTemp);
        if (!bsReaderTempFile.exists()) {
            Log.d("print", "准备创建/TbsReaderTemp！！");
            boolean mkdir = bsReaderTempFile.mkdir();
            if (!mkdir) {
                Log.d("print", "创建/TbsReaderTemp失败！！！！！");
            }
        }
        Bundle bundle = new Bundle();
   /*   1.TbsReader: Set reader view exception:Cannot add a null child view to a ViewGroup
        TbsReaderView: OpenFile failed! [可能是文件的路径错误]*/
   /*   2.插件加载失败
        so文件不支持;*/
   /*
   ndk {
            //设置支持的SO库架构  'arm64-v8a',
            abiFilters 'armeabi', "armeabi-v7a",  'x86'
        } */
   /*
        3.自适应大小

    */
        bundle.putString("filePath", filePath);
        bundle.putString("tempPath", tbsReaderTemp);
        boolean result = mTbsReaderView.preOpen(getFileType(fileName), false);
        Log.d("print", "查看文档---" + result);
        if (result) {
            mTbsReaderView.openFile(bundle);
        } else {

        }
    }


    /***
     * 获取文件类型
     *
     * @param path 文件路径
     * @return 文件的格式
     */
    private String getFileType(String path) {
        String str = "";

        if (TextUtils.isEmpty(path)) {
            return str;
        }
        int i = path.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = path.substring(i + 1);
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTbsReaderView.onStop();
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }
}
