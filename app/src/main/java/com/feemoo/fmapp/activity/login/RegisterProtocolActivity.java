package com.feemoo.fmapp.activity.login;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.weicong.library.PWebView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册协议
 */
public class RegisterProtocolActivity extends BaseActivity {
    @BindView(R.id.webView)
    PWebView mWebView;
    private String url;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_protocol);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            title = bundle.getString("title");
        }
        initUI();
        // mWebView.loadUrl(url);

    }

    private void initUI() {
        mWebView.loadUrl(url);
        mWebView.hideBrowserController();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvTitle.setText(title);
    }

    @OnClick(R.id.webView)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.webView:
                onfresh();
                break;
        }
    }

    private void onfresh() {
        mWebView.loadUrl(url);
    }
}
