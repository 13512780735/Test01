package com.fmapp.test01.activity.login;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.fmapp.test01.R;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.network.Appconst.AppConst;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.LoginCodeModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.DensityUtil;
import com.fmapp.test01.utils.StatusBarUtil;
import com.fmapp.test01.utils.StringUtil;
import com.fmapp.test01.widght.BorderTextView;
import com.fmapp.test01.widght.CircleImageView;

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

/**
 * 注册一步骤
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.myScrollView)
    NestedScrollView myScrollView;
    @BindView(R.id.titleBgRl)
    RelativeLayout titleBgRl;
    @BindView(R.id.rl_header)
    RelativeLayout mRlHeader;
    @BindView(R.id.btn_title_left)
    TextView mTitle;
    @BindView(R.id.iv01)
    CircleImageView iv01;
    @BindView(R.id.ll01)
    LinearLayout ll01;
    @BindView(R.id.mEtName)
    ClearEditText mEtName;//请输入用户名
    @BindView(R.id.mEtPass)
    ClearEditText mEtPass;//请输入密码
    @BindView(R.id.send_code_btn)
    BorderTextView mSendCode;//请输入密码
    TimeCount time = new TimeCount(30000, 1000);
    private String mobile;
    private String msgid = "2634140";
    private String code;
    private String pcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        int color = getResources().getColor(R.color.gray_background_color);
        StatusBarUtil.setColor(this, color, 0);
        StatusBarUtil.setLightMode(this);
        initUI();
    }

    private void initUI() {
        Drawable drawable = getResources().getDrawable(
                R.mipmap.icon_back);
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
                mTitle.setText("注册");
                mTitle.setAlpha(0);
                iv01.setVisibility(View.VISIBLE);
            } else if (i1 > 0 && i1 < height) {
                mTitle.setText("注册");
                //获取渐变率
                float scale = (float) i1 / height;
                //获取渐变数值
                float alpha = (1.0f * scale);
                mTitle.setAlpha(alpha);
                iv01.setVisibility(View.INVISIBLE);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                mTitle.setCompoundDrawables(drawable, null, null, null);
            } else {
                mTitle.setAlpha(1f);
            }

        });
    }

    @OnClick({R.id.send_code_btn, R.id.mTvCode, R.id.mTvNext, R.id.btn_title_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_title_left:
                onBackPressed();
                break;
            case R.id.send_code_btn:
                sendCode();
                break;
            case R.id.mTvCode:
                //  ToastCustom.showToast(mContext, "请检查您输入的手机号是否有效");
                showToast("请检查您输入的手机号是否有效");
                break;
            case R.id.mTvNext:
                mobile = mEtName.getText().toString().trim();
                code = mEtPass.getText().toString().trim();
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
                Log.d("msgid222", msgid);
                if (StringUtil.isBlank(msgid)) {
                    // ToastCustom.showToast(mContext,"验证码不能为空");
                    showToast("账号或验证码不正确，长度请在6-20之间");
                    return;
                }
                toGet(mobile, code);


                break;
        }
    }

    private void toGet(String mobile, String code) {
        LoaddingShow();
        pcode = "+86";

        String url = AppConst.BASE_URL + "user/checkregphone";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("pcode", pcode)
                .add("phone", mobile)
                .add("ucode", code)
                .add("msgid", msgid)
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
                        Bundle bundle = new Bundle();
                        bundle.putString("msgid", msgid);
                        bundle.putString("pcode", pcode);
                        bundle.putString("code", code);
                        bundle.putString("phone", mobile);
                        toActivity(Register02Activity.class, bundle);
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
        RetrofitUtil.getInstance().getregcode("+86", mobile, new Subscriber<BaseResponse<LoginCodeModel>>() {
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
                if (baseResponse.getStatus() == 1) {
                    msgid = baseResponse.getData().getMsgid();
                    Log.d("msgid", msgid);
                    // ToastCustom.showToast(mContext, baseResponse.getMsg());
                    showToast(baseResponse.getMsg());
                } else {
                    showToast(baseResponse.getMsg());
                    //  ToastCustom.showToast(mContext, baseResponse.getMsg());
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
