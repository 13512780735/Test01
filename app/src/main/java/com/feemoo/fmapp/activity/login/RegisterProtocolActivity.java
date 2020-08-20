package com.feemoo.fmapp.activity.login;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册协议
 */
public class RegisterProtocolActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_protocol);
    }
    @OnClick(R.id.webView)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.webView:
                onfresh();
                break;
        }
    }

    private void onfresh() {
        //mWebView.loadUrl("file:///android_asset/Registeragreement.html");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LoaddingShow();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LoaddingDismiss();
                super.onPageFinished(view, url);
            }
        });
    }
}
