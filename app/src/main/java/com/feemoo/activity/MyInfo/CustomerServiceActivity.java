package com.feemoo.activity.MyInfo;


import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.feemoo.R;
import com.feemoo.base.BaseActivity;
import com.feemoo.utils.CustomDialog;
import com.gyf.immersionbar.ImmersionBar;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class CustomerServiceActivity extends BaseActivity {
    @BindView(R.id.tvService)
    TextView mTvService;
    @BindView(R.id.tvUrl)
    TextView mTvUrl;
    @BindView(R.id.rlQQ)
    RelativeLayout rlQQ;
    @BindView(R.id.rlUrl)
    RelativeLayout rlUrl;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initView();
    }

    private void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mTvService.setText("800180405");
        mTvUrl.setText("service@feemoo.com");
        rlQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CustomDialog dialog1 = new CustomDialog(mContext).builder()
                        .setGravity(Gravity.CENTER)
                        .setTitle("", mContext.getResources().getColor(R.color.black))//可以不设置标题颜色，默认系统颜色
                        .setSubTitle("飞猫云想要打开QQ")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定",  new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isQQInstall(mContext)) {
                                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + mTvService.getText().toString().trim();//uin是发送过去的qq号码
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                                } else {
                                    showToast("请安装QQ客户端");
                                }
                            }
                        });
                dialog1.show();
            }
        });
        rlUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(mTvUrl.getText().toString());
                showToast("本文件分享地址已复制剪切板，请前往粘贴使用");
            }
        });

    }

    public static boolean isQQInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                //通过遍历应用所有包名进行判断
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

}
