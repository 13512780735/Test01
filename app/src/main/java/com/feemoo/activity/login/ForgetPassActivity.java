package com.feemoo.activity.login;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.feemoo.R;
import com.feemoo.base.BaseActivity;
import com.feemoo.network.Appconst.AppConst;
import com.feemoo.network.model.BaseResponse;
import com.feemoo.network.model.LoginCodeModel;
import com.feemoo.network.util.DataResultException;
import com.feemoo.network.util.RetrofitUtil;
import com.feemoo.phoneArea.PhoneAreaCodeActivity;
import com.feemoo.utils.StringUtil;
import com.feemoo.utils.Utils;
import com.feemoo.widght.BorderTextView;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nzy.library.ClearEditText;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rx.Subscriber;

public class ForgetPassActivity extends BaseActivity {
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.mEtName)
    ClearEditText mEtName;//请输入用户名
    @BindView(R.id.mEtPass)
    ClearEditText mEtPass;//请输入验证码
    @BindView(R.id.send_code_btn)
    BorderTextView mSendCode;
    @BindView(R.id.mTvName)
    TextView mTvName;
    @BindView(R.id.mEdNewPass)
    ClearEditText mEdNewPass;
    TimeCount time = new TimeCount(30000, 1000);
    String tel = "+86";
    private String mobile;
    private String code;
    private String pass;
    private String msgid;
    private String pcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
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

        mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PhoneAreaCodeActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @OnClick({R.id.send_code_btn, R.id.mTvNext})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_code_btn:
                if (Utils.isFastClick()) {
                    sendCode();
                }
                break;
            case R.id.mTvNext:
                if (Utils.isFastClick()) {
                    mobile = mEtName.getText().toString().trim();
                    code = mEtPass.getText().toString().trim();
                    pass = mEdNewPass.getText().toString().trim();
                    if (StringUtil.isBlank(mobile)) {
                        //ToastCustom.showToast(mContext,"手机号不能为空");
                        showToast("手机号不能为空");
                        return;
                    }
                    if (StringUtil.isBlank(code)) {
                        // ToastCustom.showToast(mContext,"验证码不能为空");
                        showToast("验证码不能为空");
                        return;
                    }
                    if (StringUtil.isBlank(pass)) {
                        // ToastCustom.showToast(mContext,"验证码不能为空");
                        showToast("新密码不能为空");
                        return;
                    }
                    if (StringUtil.isBlank(msgid)) {
                        // ToastCustom.showToast(mContext,"验证码不能为空");
                        showToast("账号或验证码不正确，长度请在6-20之间");
                        return;
                    }
                    toGet(mobile, code, pass);

                }
                break;
        }
    }

    private void toGet(String mobile, String code, String pass) {
        LoaddingShow();
        pcode = tel;
        String url = AppConst.BASE_URL + "user/phonemodipass";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("pcode", pcode)
                .add("phone", mobile)
                .add("ucode", code)
                .add("msgid", msgid)
                .add("pass", pass)
                .build();
        Request request = new Request
                .Builder()
                .url(url)//要访问的链接
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

    }

    private void sendCode() {
        mobile = mEtName.getText().toString().trim();
        if (StringUtil.isBlank(mobile)) {
            // ToastCustom.showToast(mContext, "手机号不能为空");
            showToast("手机号不能为空");
            return;
        } else {
            // SMSSDK.getVerificationCode("86", mobile);
            VerificationCode(mobile);
            time.start();
            LoaddingShow();
        }
    }

    /**
     * 获取验证码
     *
     * @param mobile
     */
    private void VerificationCode(String mobile) {
        RetrofitUtil.getInstance().getlogincode(tel, mobile, new Subscriber<BaseResponse<LoginCodeModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoaddingDismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    showToast(resultException.getMsg());
                }
            }

            @Override
            public void onNext(BaseResponse<LoginCodeModel> baseResponse) {
                Log.d("数据", baseResponse.getData().toString());
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    msgid = baseResponse.getData().getMsgid();
                    showToast(baseResponse.getMsg());
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mSendCode.setText("获取验证码");
            mSendCode.setClickable(true);
            mSendCode.setContentColorResource01(getResources().getColor(R.color.button_confirm));
            mSendCode.setStrokeColor01(getResources().getColor(R.color.button_confirm));
            mSendCode.setTextColor(getResources().getColor(R.color.white));
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mSendCode.setClickable(false);//防止重复点击
            mSendCode.setText("重新获取" + millisUntilFinished / 1000 + "s");
            mSendCode.setContentColorResource01(getResources().getColor(R.color.gray_background_color));
            mSendCode.setStrokeColor01(getResources().getColor(R.color.gray_background_color));
            mSendCode.setTextColor(getResources().getColor(R.color.text_color));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            Bundle bundle = data.getExtras();
            tel = bundle.getString("tel");
            mTvName.setText(tel);
        }
    }
}
