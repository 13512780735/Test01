package com.feemoo.fmapp.activity;


import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.login.Register02Activity;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.Appconst.AppConst;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.widght.BorderTextView;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rx.Subscriber;

public class AuthorizationLoginActivity extends BaseActivity {

    private String cd;
    @BindView(R.id.tvConfirm)
    BorderTextView tvConfirm;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.status_bar_view)
    View status_bar_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization_login);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cd = bundle.getString("cd");
        }
        initUI();
    }

    private void initUI() {
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLogin(cd);
            }
        });
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void toLogin(String cd) {
        LoaddingShow();
        String url = AppConst.BASE_URL + "user/scanlogin";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("code", cd)
                .build();
        Request request = new Request
                .Builder()
                .url(url)//要访问的链接
                .addHeader("token", token)
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoaddingDismiss();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                LoaddingDismiss();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if (status == 1) {
                        finish();
                        Looper.prepare();
                        showToast(msg);
                        Looper.loop();


                    } else {
                        Looper.prepare();
                        showToast(msg);
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


//        RetrofitUtil.getInstance().scanlogin(token, cd, new Subscriber<BaseResponse<String>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                LoaddingDismiss();
//                if (e instanceof DataResultException) {
//                    DataResultException resultException = (DataResultException) e;
//                    showToast(resultException.getMsg());
//                }
//            }
//
//            @Override
//            public void onNext(BaseResponse<String> baseResponse) {
//                LoaddingDismiss();
//                if ("1".equals(baseResponse)) {
//                    finish();
//                    showToast(baseResponse.getMsg());
//                } else {
//                    showToast(baseResponse.getMsg());
//                }
//            }
//        });
    }
}
