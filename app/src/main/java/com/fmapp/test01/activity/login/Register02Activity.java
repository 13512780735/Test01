package com.fmapp.test01.activity.login;


import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.fmapp.test01.R;
import com.fmapp.test01.activity.main.MainActivity;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.LoginRegisterModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.AppManager;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.fmapp.test01.utils.StringUtil;
import com.fmapp.test01.widght.BorderTextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.nzy.library.ClearEditText;
import rx.Subscriber;

/**
 * 注册二步骤
 */
public class Register02Activity extends BaseActivity {
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.mTvLogin)
    BorderTextView mTvLogin;
    @BindView(R.id.llPass)
    LinearLayout llPass;
    @BindView(R.id.mIvName)
    ImageView mIvName;
    @BindView(R.id.mEtName)
    ClearEditText mEtName;
    @BindView(R.id.mVName)
    View mVName;
    @BindView(R.id.mIvPass)
    ImageView mIvPass;
    @BindView(R.id.mEtPass)
    ClearEditText mEtPass;
    @BindView(R.id.mVPass)
    View mVPass;
    @BindView(R.id.mIvPasss)
    ImageView mIvPasss;
    @BindView(R.id.mEtPasss)
    ClearEditText mEtPasss;
    @BindView(R.id.mVPasss)
    View mVPasss;

    @BindView(R.id.checkbox01)
    CheckBox checkBox;

    private int flag;
    private String msgid,pcode,code,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register02);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            msgid=bundle.getString("msgid");
            pcode=bundle.getString("pcode");
            code=bundle.getString("code");
            mobile=bundle.getString("mobile");
        }
        initUI();
    }

    private void initUI() {
        mRadioGroup.check(mRadioGroup.getChildAt(0).getId());
        flag = 0;
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.mRb01:
                        mTvLogin.setText("同意协议并注册");
                        llPass.setVisibility(View.VISIBLE);
                        mVPasss.setVisibility(View.VISIBLE);
                        flag = 0;
                        break;
                    case R.id.mRb02:
                        mTvLogin.setText("绑定并登录");
                        llPass.setVisibility(View.INVISIBLE);
                        mVPasss.setVisibility(View.INVISIBLE);
                        flag = 1;
                        break;
                }
            }
        });

        if (!checkBox.isChecked()) {
            showToast("请您仔细阅读并且点击同意《用户协议》方可登录");
            return;
        }

//        mEtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                mIvName.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user01));
//                mVName.setBackgroundColor(getResources().getColor(R.color.black));
////                if (b) {
////                    mIvName.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user01));
////                    mVName.setBackgroundColor(getResources().getColor(R.color.black));
////                } else {
////                    mIvName.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user));
////                    mVName.setBackgroundColor(getResources().getColor(R.color.line));
////                }
//                inTextWatcher();
//            }
//        });
//        mEtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                mIvPass.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pass01));
//                mVPass.setBackgroundColor(getResources().getColor(R.color.black));
////                if (b) {
////                    mIvPass.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pass01));
////                    mVPass.setBackgroundColor(getResources().getColor(R.color.black));
////                } else {
////                    mIvPass.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pass));
////                    mVPass.setBackgroundColor(getResources().getColor(R.color.line));
////                }
//                inTextWatcher();
//            }
//        });
//        mEtPasss.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                mIvPasss.setImageDrawable(getResources().getDrawable(R.mipmap.icon_passs02));
//                mVPasss.setBackgroundColor(getResources().getColor(R.color.black));
////                if (b) {
////
////                    mIvPasss.setImageDrawable(getResources().getDrawable(R.mipmap.icon_passs02));
////                    mVPasss.setBackgroundColor(getResources().getColor(R.color.black));
////                } else {
////                    mIvPasss.setImageDrawable(getResources().getDrawable(R.mipmap.icon_passs));
////                    mVPasss.setBackgroundColor(getResources().getColor(R.color.line));
////                }
//                inTextWatcher();
//            }
//        });


    }

    private void inTextWatcher() {
        String Names = mEtName.getText().toString();
        String Pass = mEtPass.getText().toString();
        String Passs = mEtPasss.getText().toString();
        if (Names.length() > 0) {
            mIvName.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user01));
            mVName.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            mIvName.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user));
            mVName.setBackgroundColor(getResources().getColor(R.color.line));
        }
        if (Pass.length() > 0) {
            mIvPass.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pass01));
            mVPass.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            mIvPass.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pass));
            mVPass.setBackgroundColor(getResources().getColor(R.color.line));
        }
        if (Passs.length() > 0) {
            mIvPasss.setImageDrawable(getResources().getDrawable(R.mipmap.icon_passs02));
            mVPasss.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            mIvPasss.setImageDrawable(getResources().getDrawable(R.mipmap.icon_passs));
            mVPasss.setBackgroundColor(getResources().getColor(R.color.line));
        }
//        mEtName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                boolean length=mEtName.getText().length()>0;
//                if(length){
//                    mIvName.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user01));
//                    mVName.setBackgroundColor(getResources().getColor(R.color.black));
//                }else {
//                    mIvName.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user));
//                    mVName.setBackgroundColor(getResources().getColor(R.color.line));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        mEtPass.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                boolean length=mEtPass.getText().length()>0;
//                if(length){
//                    mIvPass.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pass01));
//                    mVPass.setBackgroundColor(getResources().getColor(R.color.black));
//                }else {
//                    mIvPass.setImageDrawable(getResources().getDrawable(R.mipmap.icon_pass));
//                    mVPass.setBackgroundColor(getResources().getColor(R.color.line));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        mEtPasss.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                boolean length=mEtPasss.getText().length()>0;
//                if(length){
//                    mIvPasss.setImageDrawable(getResources().getDrawable(R.mipmap.icon_passs02));
//                    mVPasss.setBackgroundColor(getResources().getColor(R.color.black));
//                }else {
//                    mIvPasss.setImageDrawable(getResources().getDrawable(R.mipmap.icon_passs));
//                    mVPasss.setBackgroundColor(getResources().getColor(R.color.line));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    @OnClick({R.id.mTvLogin, R.id.protocol_tv01})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mTvLogin:

                String moblies=mEtName.getText().toString().trim();
                String pass=mEtPass.getText().toString().trim();
                String passs=mEtPasss.getText().toString().trim();
                if(flag==0){
                    if(StringUtil.isBlank(moblies)){
                        showToast("请输入账号");
                        return;
                    }
                    if(StringUtil.isBlank(pass)){
                        showToast("请输入密码");
                        return;
                    }
                    if(StringUtil.isBlank(passs)){
                        showToast("请再次输入密码");
                        return;
                    }
                    toLogin(moblies,pass,passs);
                }else {
                    if(StringUtil.isBlank(moblies)){
                        showToast("请输入账号");
                        return;
                    }
                    if(StringUtil.isBlank(pass)){
                        showToast("请输入密码");
                        return;
                    }
                    toLogin01(moblies,pass);
                }
                break;
            case R.id.protocol_tv01:
                break;
        }
    }

    /**
     * 绑定已有账号登陆
     * @param moblies
     * @param pass
     */
    private void toLogin01(String moblies, String pass) {
       LoaddingShow();
       RetrofitUtil.getInstance().phonebind(pcode, mobile, code, msgid, moblies, pass, new Subscriber<BaseResponse<LoginRegisterModel>>() {
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
               LoaddingDismiss();
               if(baseResponse.getStatus()==1){
                   String token=baseResponse.getData().getToken();
                   SharedPreferencesUtils.put(mContext,"token",token);
                   showToast(baseResponse.getMsg());
                   toActivityFinish(MainActivity.class);
                   AppManager.getAppManager().finishAllActivity();
               }else {
                   showToast(baseResponse.getMsg());
               }
           }
       });
    }

    /**
     * 新注册登录
     * @param moblies
     * @param pass
     * @param passs
     */
    private void toLogin(String moblies, String pass, String passs) {
        LoaddingShow();
        RetrofitUtil.getInstance().phonereg(pcode, mobile, code, msgid, moblies, pass, passs, new Subscriber<BaseResponse<LoginRegisterModel>>() {
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
                LoaddingDismiss();
                if(baseResponse.getStatus()==1){
                    String token=baseResponse.getData().getToken();
                    SharedPreferencesUtils.put(mContext,"token",token);
                    showToast(baseResponse.getMsg());
                    toActivityFinish(MainActivity.class);
                    AppManager.getAppManager().finishAllActivity();
                }else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }
}
