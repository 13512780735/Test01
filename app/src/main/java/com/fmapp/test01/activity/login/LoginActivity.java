package com.fmapp.test01.activity.login;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.fmapp.test01.R;
import com.fmapp.test01.activity.main.MainActivity;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.LoginRegisterModel;
import com.fmapp.test01.network.model.LoginCodeModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.DensityUtil;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.fmapp.test01.utils.StatusBarUtil;
import com.fmapp.test01.utils.StringUtil;
import com.fmapp.test01.widght.BorderTextView;
import com.fmapp.test01.widght.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nzy.library.ClearEditText;
import rx.Subscriber;

/**
 * 登录界面
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.myScrollView)
    NestedScrollView myScrollView;
    @BindView(R.id.ll01)
    LinearLayout ll01;
    @BindView(R.id.titleBgRl)
    RelativeLayout titleBgRl;
    @BindView(R.id.rl_header)
    RelativeLayout mRlHeader;
    @BindView(R.id.btn_title_left)
    TextView mTitle;
    @BindView(R.id.iv01)
    CircleImageView iv01;
    @BindView(R.id.mTvSelect)
    TextView mTvSelect;//默认用验证码登录
    @BindView(R.id.mTvName)
    TextView mTvName;//默认用户名
    @BindView(R.id.mTvCode)
    TextView mTvCode;//默认用户名
    @BindView(R.id.mTvPass)
    TextView mTvPass;//默认密码
    @BindView(R.id.mEtName)
    ClearEditText mEtName;//请输入用户名
    @BindView(R.id.mEtPass)
    ClearEditText mEtPass;//请输入密码
    @BindView(R.id.send_code_btn)
    BorderTextView mSendCode;
    @BindView(R.id.checkbox01)
    CheckBox checkBox;

    int flag = 0;
    TimeCount time = new TimeCount(30000, 1000);
    private String mobile;
    private String password;
    private String msgid;
    private LoginActivity mContext;
   // private LoaddingDialog loaddingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        int color = getResources().getColor(R.color.gray_background_color);
        StatusBarUtil.setColor(this, color, 0);
        StatusBarUtil.setLightMode(this);
        setSwipeBackEnable(false);
       // loaddingDialog = new LoaddingDialog(this);
        initUI();
    }

    private void initUI() {
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        int height01 = width;//屏幕高度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlHeader.getLayoutParams();
        params.width = width;
        params.height = height01;
        mRlHeader.setLayoutParams(params);//设置配置参数
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ll01.getLayoutParams();
        params1.width = width;
        params1.height = wm.getDefaultDisplay().getHeight() - DensityUtil.dip2px(mContext, 50);
        ll01.setLayoutParams(params1);//设置配置参数
        myScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            int height = DensityUtil.dip2px(mContext, 50);
            if (i1 <= 0) {
                mTitle.setText("登录");
                mTitle.setAlpha(0);
                iv01.setVisibility(View.VISIBLE);
            } else if (i1 > 0 && i1 < height) {
                mTitle.setText("登录");
                //获取渐变率
                float scale = (float) i1 / height;
                //获取渐变数值
                float alpha = (1.0f * scale);
                mTitle.setAlpha(alpha);
                iv01.setVisibility(View.INVISIBLE);
            } else {
                mTitle.setAlpha(1f);
            }

        });

        refreshUI();

        if (!checkBox.isChecked()) {
            showToast("请您仔细阅读并且点击同意《用户协议》方可登录");
            return;
        }

    }

    private void refreshUI() {
        // 使用代码设置drawableleft
        Drawable drawable = getResources().getDrawable(
                R.mipmap.icon_down);
        if (flag == 0) {
            mTvCode.setVisibility(View.INVISIBLE);
            mTvSelect.setText("用验证码登录");
            mSendCode.setVisibility(View.GONE);
            mTvName.setText("用户名");
            mTvName.setGravity(Gravity.CENTER_VERTICAL);
            mTvName.setPadding(0, 0, 0, 0);
            mTvPass.setText("密码");
            mEtName.setHint("请输入用户名");
            mEtPass.setHint("请输入密码");
           mEtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            // 这一步必须要做，否则不会显示。
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mTvName.setCompoundDrawables(null, null, null, null);
            mEtName.setText(SharedPreferencesUtils.getString(mContext,"mobile"));
            mEtPass.setText(SharedPreferencesUtils.getString(mContext,"password"));
        } else {
            mTvCode.setVisibility(View.VISIBLE);
            mTvSelect.setText("用密码登录");
            mSendCode.setVisibility(View.VISIBLE);
            mTvName.setText("+86" + "");
            mTvName.setGravity(Gravity.CENTER);
            mTvName.setPadding(0, 0, DensityUtil.dip2px(mContext, 20), 0);
            mTvPass.setText("验证码");
            mEtName.setHint("请输入手机号");
            mEtPass.setHint("请输入验证码");
            mEtPass.setInputType(InputType.TYPE_CLASS_TEXT);
            // 这一步必须要做，否则不会显示。
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            mTvName.setCompoundDrawables(null, null, drawable, null);
            mEtName.setText(SharedPreferencesUtils.getString(mContext,"mobile"));
            mEtPass.setText(SharedPreferencesUtils.getString(mContext,"password"));
        }
    }

    @OnClick({R.id.send_code_btn, R.id.mTvLogin, R.id.mTvRegister, R.id.checkbox01, R.id.protocol_tv01, R.id.mTvSelect, R.id.mTvCode})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_code_btn:
                sendCode();
                break;
            case R.id.mTvLogin:
                if (flag == 0) {
                    mobile = mEtName.getText().toString().trim();
                    password = mEtPass.getText().toString().trim();
                    if (StringUtil.isBlank(mobile)) {
                        showToast( "用户名不能为空");
                        return;
                    }
                    if (StringUtil.isBlank(password)) {
                        showToast( "密码不能为空");
                        return;
                    }
                    if (!checkBox.isChecked()) {
                        showToast("请您仔细阅读并且点击同意《用户协议》方可登录");
                        return;
                    }
                    toAccountLogin(mobile, password);
                } else {
                    mobile = mEtName.getText().toString().trim();
                    password=mEtPass.getText().toString().trim();
                    if (StringUtil.isBlank(mobile)) {
                        showToast( "手机号不能为空");
                        return;
                    }
                    if (StringUtil.isBlank(password)) {
                        showToast("验证码不能为空");
                        return;
                    }
                    if (!checkBox.isChecked()) {
                        showToast("请您仔细阅读并且点击同意《用户协议》方可登录");
                        return;
                    }
                    toPhoneLogin(mobile, password);
                }
                break;
            case R.id.mTvRegister:
                toActivity(RegisterActivity.class);
                break;
            case R.id.checkbox01:
                break;
            case R.id.protocol_tv01:
                toActivity(RegisterProtocolActivity.class);
                break;
            case R.id.mTvCode:
                showToast( "请检查您输入的手机号是否有效");
                break;
            case R.id.mTvSelect:
                if (flag == 0) {
                    flag = 1;
                    mEtPass.setText("");
                    refreshUI();
                } else {
                    flag = 0;
                    mEtPass.setText("");
                    refreshUI();
                }
                break;
        }

    }



    /**
     * 手机登录
     * @param mobile
     * @param password
     */
    private void toPhoneLogin(String mobile, String password) {
        msgid="1";
        RetrofitUtil.getInstance().phonefastlogin("+86", mobile, password, msgid, new Subscriber<BaseResponse<LoginRegisterModel>>() {
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
            public void onNext(BaseResponse<LoginRegisterModel> baseResponse) {
                if(baseResponse.getStatus()==1){
                    String token=baseResponse.getData().getToken();
                    SharedPreferencesUtils.put(mContext,"token",token);
                    showToast(baseResponse.getMsg());
                    SharedPreferencesUtils.put(mContext,"mobile",mobile);
                    SharedPreferencesUtils.put(mContext,"password",password);
                    toActivityFinish(MainActivity.class);
                }
                showToast(baseResponse.getMsg());
            }
        });
    }

    /**
     * 账号密码登录
     * @param mobile
     * @param password
     */
    private void toAccountLogin(String mobile, String password) {
        LoaddingShow();
        RetrofitUtil.getInstance().getUserLogin(mobile, password, new Subscriber<BaseResponse<LoginRegisterModel>>() {
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
            public void onNext(BaseResponse<LoginRegisterModel> baseResponse) {
                Log.d("数据",baseResponse.getMsg());
                LoaddingDismiss();
                if(baseResponse.getStatus()==1){
                    String token=baseResponse.getData().getToken();
                    SharedPreferencesUtils.put(mContext,"token",token);
                    SharedPreferencesUtils.put(mContext,"mobile",mobile);
                    SharedPreferencesUtils.put(mContext,"password",password);
                    showToast(baseResponse.getMsg());
                    toActivityFinish(MainActivity.class);
                }else {
                    showToast(baseResponse.getMsg());
                }
            }
        });

    }

    private void sendCode() {
        mobile = mEtName.getText().toString().trim();
        if (StringUtil.isBlank(mobile)) {
            showToast(  "手机号不能为空");
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
                LoaddingDismiss();
                if(baseResponse.getStatus()==1){
                    msgid=baseResponse.getData().getMsgid();
                    showToast(baseResponse.getMsg());
                }else {
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
//    private void showToast(String msg) {
//        ToastUtil toastUtil = new ToastUtil(mContext, R.layout.toast_center, msg);
//        toastUtil.show();
//    }
//    public void LoaddingDismiss() {
//        if (loaddingDialog != null && loaddingDialog.isShowing()) {
//            loaddingDialog.dismiss();
//        }
//    }
//
//    public void LoaddingShow() {
//        if (loaddingDialog == null) {
//            loaddingDialog = new LoaddingDialog(this);
//        }
//
//        if (!loaddingDialog.isShowing()) {
//            loaddingDialog.show();
//        }
//    }
}
