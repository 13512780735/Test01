package com.feemoo.fmapp.activity.MyInfo;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.login.LoginActivity;
import com.feemoo.fmapp.activity.login.RegisterActivity;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.LoginCodeModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.AppManager;
import com.feemoo.fmapp.utils.StringUtil;
import com.feemoo.fmapp.utils.Utils;
import com.feemoo.fmapp.widght.BorderTextView;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nzy.library.ClearEditText;
import rx.Subscriber;

public class BindPhoneActivity extends BaseActivity {
    @BindView(R.id.mEdPhone)
    ClearEditText mEdPhone;//请输入用户名
    @BindView(R.id.mEtCode)
    ClearEditText mEtCode;//请输入用户名
    @BindView(R.id.send_code_btn)
    BorderTextView mSendCode;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    TimeCount time = new TimeCount(30000, 1000);
    private String msgid;
    private String mobile;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
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
                    mobile = mEdPhone.getText().toString().trim();
                    code = mEtCode.getText().toString().trim();
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
                    if (StringUtil.isBlank(msgid)) {
                        // ToastCustom.showToast(mContext,"验证码不能为空");
                        showToast("账号或验证码不正确，长度请在6-20之间");
                        return;
                    }
                    toGet(mobile, code);

                }
                break;
        }
    }

    private void toGet(String mobile, String code) {
        LoaddingShow();
        RetrofitUtil.getInstance().phonemodipass(token, "+86", mobile, code, msgid, new Subscriber<BaseResponse<String>>() {
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
            public void onNext(BaseResponse<String> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    showToast(baseResponse.getMsg());
                    toActivityFinish(LoginActivity.class);
                    AppManager.getAppManager().finishAllActivity();
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    private void sendCode() {
        mobile = mEdPhone.getText().toString().trim();
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
        RetrofitUtil.getInstance().getlogincode("+86", mobile, new Subscriber<BaseResponse<LoginCodeModel>>() {
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
                    Log.d("msgid", msgid);
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
            mSendCode.setContentColorResource01(getResources().getColor(R.color.yellow_button_bg_pressed_color));
            mSendCode.setStrokeColor01(getResources().getColor(R.color.yellow_button_bg_pressed_color));
            mSendCode.setTextColor(getResources().getColor(R.color.black));
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
}
