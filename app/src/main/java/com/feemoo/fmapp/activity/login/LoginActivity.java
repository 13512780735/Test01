package com.feemoo.fmapp.activity.login;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.main.MainActivity;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.Appconst.AppConst;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.LoginRegisterModel;
import com.feemoo.fmapp.network.model.LoginCodeModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.phoneArea.PhoneAreaCodeActivity;
import com.feemoo.fmapp.utils.DensityUtil;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.feemoo.fmapp.utils.StatusBarUtil;
import com.feemoo.fmapp.utils.StringUtil;
import com.feemoo.fmapp.utils.Utils;
import com.feemoo.fmapp.widght.BorderTextView;
import com.feemoo.fmapp.widght.CircleImageView;
import com.gyf.immersionbar.ImmersionBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nzy.library.ClearEditText;
import rx.Subscriber;

/**
 * 登录界面
 */

public class LoginActivity extends BaseActivity {
    String TAG = "LoginActivity";
    @BindView(R.id.myScrollView)
    NestedScrollView myScrollView;
    @BindView(R.id.ll01)
    LinearLayout ll01;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.rl_header)
    RelativeLayout mRlHeader;
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
    @BindView(R.id.protocol_tv)
    TextView protocol_tv;//默认密码
    @BindView(R.id.mEtName)
    ClearEditText mEtName;//请输入用户名
    @BindView(R.id.mEtPass)
    ClearEditText mEtPass;//请输入密码
    @BindView(R.id.send_code_btn)
    BorderTextView mSendCode;
    @BindView(R.id.checkbox01)
    CheckBox checkBox;
    @BindView(R.id.llWchat)
    LinearLayout llWchat;
    @BindView(R.id.llQQ)
    LinearLayout llQQ;
    @BindView(R.id.llPhone)
    LinearLayout llPhone;

    int flag = 0;
    TimeCount time = new TimeCount(30000, 1000);
    private String mobile;
    private String password;
    private String msgid;
    private LoginActivity mContext;
    // private LoaddingDialog loaddingDialog;
    String tel = "+86";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        if (ImmersionBar.isSupportStatusBarDarkFont()) {
            ImmersionBar.with(this).statusBarDarkFont(true).init();
        } else {

            Log.d("LoginActivity", "当前设备不支持状态栏字体变色");
        }
        setSwipeBackEnable(false);
        initUI();
    }

    private void initUI() {
        mToolbar.setAlpha(0);
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        int height01 = width / 2 + 40;//屏幕高度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlHeader.getLayoutParams();
        params.width = width;
        params.height = height01;
        mRlHeader.setLayoutParams(params);//设置配置参数
//        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ll01.getLayoutParams();
//        params1.width = width;
//        params1.height = wm.getDefaultDisplay().getHeight() - DensityUtil.dip2px(mContext, 50);
//        ll01.setLayoutParams(params1);//设置配置参数
//        myScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
//            int height = DensityUtil.dip2px(mContext, 50);
//            if (i1 <= 0) {
//                mToolbar.setTitle("登录");
//                mToolbar.setAlpha(0);
//                iv01.setVisibility(View.VISIBLE);
//            } else if (i1 > 0 && i1 < height) {
//                mToolbar.setTitle("登录");
//                //获取渐变率
//                float scale = (float) i1 / height;
//                //获取渐变数值
//                float alpha = (1.0f * scale);
//                mToolbar.setAlpha(alpha);
//                iv01.setVisibility(View.INVISIBLE);
//            } else {
//                mToolbar.setAlpha(1f);
//            }
//
//        });
        String str1 = "登录即表示您已阅读并同意<font color='#326ef3'>《用户协议》</font>";
        protocol_tv.setText(Html.fromHtml(str1));
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
            mTvCode.setVisibility(View.VISIBLE);
            mTvCode.setText("忘记密码？");
            mTvSelect.setText("");
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
            mEtName.setText(SharedPreferencesUtils.getString(mContext, "mobile"));
            mEtPass.setText(SharedPreferencesUtils.getString(mContext, "password"));
        } else {
            mTvCode.setVisibility(View.VISIBLE);
            mTvCode.setText("收不到验证码？");
            mTvSelect.setText("用密码登录");
            mSendCode.setVisibility(View.VISIBLE);
            mTvName.setText(tel);
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
            mEtName.setText(SharedPreferencesUtils.getString(mContext, "mobile"));
            mEtPass.setText(SharedPreferencesUtils.getString(mContext, "password"));
            mTvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, PhoneAreaCodeActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }

    @OnClick({R.id.send_code_btn, R.id.mTvLogin, R.id.mTvRegister, R.id.checkbox01, R.id.protocol_tv, R.id.mTvSelect, R.id.mTvCode, R.id.llWchat, R.id.llQQ, R.id.llPhone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_code_btn:
                if (Utils.isFastClick()) {
                    sendCode();
                }
                break;
            case R.id.mTvLogin:
                if (Utils.isFastClick()) {
                    //MobclickAgent.event("Bike") // 事件I
                    MobclickAgent.onEvent(mContext, "login");
                    if (flag == 0) {
                        mobile = mEtName.getText().toString().trim();
                        password = mEtPass.getText().toString().trim();
                        if (StringUtil.isBlank(mobile)) {
                            showToast("用户名不能为空");
                            return;
                        }
                        if (StringUtil.isBlank(password)) {
                            showToast("密码不能为空");
                            return;
                        }
                        if (!checkBox.isChecked()) {
                            showToast("请您仔细阅读并且点击同意《用户协议》方可登录");
                            return;
                        }
                        toAccountLogin(mobile, password);
                    } else {
                        mobile = mEtName.getText().toString().trim();
                        password = mEtPass.getText().toString().trim();
                        if (StringUtil.isBlank(mobile)) {
                            showToast("手机号不能为空");
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
                }
                break;
            case R.id.mTvRegister:
                if (Utils.isFastClick()) {
                    toActivity(RegisterActivity.class);
                }
                break;
            case R.id.checkbox01:

                break;
            case R.id.protocol_tv:
                if (Utils.isFastClick()) {
                    String url = AppConst.BASE_URL01 + "archives/125";
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    bundle.putString("title", "用户协议");
                    toActivity(RegisterProtocolActivity.class, bundle);
                }
                break;
            case R.id.mTvCode:
                if (flag == 0) {
                    toActivity(ForgetPassActivity.class);
                } else {
                    showToast("请检查您输入的手机号是否有效");
                }
                break;
            case R.id.mTvSelect:
                flag = 0;
                mEtPass.setText("");
                refreshUI();
//                if (flag == 0) {
//
//                } else {
//
//                }
                break;
            case R.id.llWchat:
                authorization(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.llQQ:
                authorization(SHARE_MEDIA.QQ);
                break;
            case R.id.llPhone:
                flag = 1;
                mEtPass.setText("");
                refreshUI();

                break;
        }

    }

    //授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d(TAG, "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                //Log.d(TAG, "onComplete " + "授权完成");
                showToast("授权完成");
                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");

                Toast.makeText(getApplicationContext(), "name=" + name + ",gender=" + gender, Toast.LENGTH_SHORT).show();

                //拿到信息去请求登录接口。。。
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
              //  Log.d(TAG, "onError " + );
                showToast("授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
               // Log.d(TAG, "onCancel " + "授权取消");
                showToast("授权取消");
            }
        });
    }


    /**
     * 手机登录
     *
     * @param mobile
     * @param password
     */
    private void toPhoneLogin(String mobile, String password) {
       // msgid = "1";
        RetrofitUtil.getInstance().phonefastlogin(tel, mobile, password, msgid, new Subscriber<BaseResponse<LoginRegisterModel>>() {
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
                if ("1".equals(baseResponse.getStatus())) {
                    String token = baseResponse.getData().getToken();
                    SharedPreferencesUtils.put(mContext, "token", token);
                    showToast(baseResponse.getMsg());
                    SharedPreferencesUtils.put(mContext, "mobile", mobile);
                    SharedPreferencesUtils.put(mContext, "password", password);
                    toActivityFinish(MainActivity.class);
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    /**
     * 账号密码登录
     *
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
                Log.d("数据", baseResponse.getMsg());
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    String token = baseResponse.getData().getToken();
                    SharedPreferencesUtils.put(mContext, "token", token);
                    SharedPreferencesUtils.put(mContext, "mobile", mobile);
                    SharedPreferencesUtils.put(mContext, "password", password);
                    showToast(baseResponse.getMsg());
                    toActivityFinish(MainActivity.class);
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });

    }

    private void sendCode() {
        mobile = mEtName.getText().toString().trim();
        if (StringUtil.isBlank(mobile)) {
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
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(LoginActivity.this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            Bundle bundle = data.getExtras();
            tel = bundle.getString("tel");
            mTvName.setText(tel);
        }
    }
}
