package com.feemoo.fragment.main;


import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.feemoo.R;
import com.feemoo.activity.MyInfo.BindPhoneActivity;
import com.feemoo.activity.MyInfo.CardPackageActivity;
import com.feemoo.activity.MyInfo.CustomerServiceActivity;
import com.feemoo.activity.MyInfo.MemberActivity;
import com.feemoo.activity.MyInfo.MessageActivity;
import com.feemoo.activity.MyInfo.SettingActivity;
import com.feemoo.activity.MyInfo.SignInActivity;
import com.feemoo.activity.MyInfo.VipInfoActivity;
import com.feemoo.base.BaseFragment;
import com.feemoo.network.model.BaseResponse;
import com.feemoo.network.model.MemberModel;
import com.feemoo.network.util.DataResultException;
import com.feemoo.network.util.RetrofitUtil;
import com.feemoo.widght.CircleImageView;

import rx.Subscriber;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout ll01;
    private CircleImageView ivHeader;
    private RelativeLayout mRlHeader;
    private TextView mTitle;
    private boolean isGetData = false;
    private TextView mTvVip, mTvEndTime;
    private String avatar;
    private String Isbindphone;
    private Bundle bundle;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            GetData();
            Log.d("Animation11", "执行");
        } else {
            isGetData = false;
            Log.d("Animation12", "执行");
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onResume() {
        super.onResume();
        // ImmersionBar.with(this).statusBarDarkFont(true).init();
        GetData();
    }

    private void GetData() {
        RetrofitUtil.getInstance().getMemberInfo(token, new Subscriber<BaseResponse<MemberModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    showToast(resultException.getMsg());
                }
            }

            @Override
            public void onNext(BaseResponse<MemberModel> baseResponse) {
                if ("1".equals(baseResponse.getStatus())) {
                    refreshUI(baseResponse.getData());
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });

    }

    private void refreshUI(MemberModel data) {
        avatar = data.getLogo();
        mTitle.setText(data.getUsername());
        mTvVip.setText(data.getSlevel());
        mTvEndTime.setText(data.getEndtime());
        Isbindphone = String.valueOf(data.getIsbindphone());
        Glide.with(getActivity()).load(data.getLogo()).into(ivHeader);
    }

    public static MyFragment newInstance() {
        return new MyFragment();
    }


    private LinearLayout llVip, llUserInfo, llQianDao, llCard, llMessage, llBindPhone, llKeFu, llSetting;

    @Override
    protected void initView(View view) {

        ll01 = findView(R.id.ll01);
        ivHeader = findView(R.id.iv01);
        mRlHeader = findView(R.id.rl_header);
        mTitle = findView(R.id.tvName);
        mTitle.setVisibility(View.VISIBLE);


        mTvVip = findView(R.id.tvVip);
        mTvEndTime = findView(R.id.tvEndTime);
        llVip = findView(R.id.llVip);
        llUserInfo = findView(R.id.llUserInfo);
        llQianDao = findView(R.id.llQianDao);
        llCard = findView(R.id.llCard);
        llMessage = findView(R.id.llMessage);
        llBindPhone = findView(R.id.llBindPhone);
        llKeFu = findView(R.id.llKeFu);
        llSetting = findView(R.id.llSetting);
        llVip.setOnClickListener(this);
        llUserInfo.setOnClickListener(this);
        llQianDao.setOnClickListener(this);
        llCard.setOnClickListener(this);
        llMessage.setOnClickListener(this);
        llBindPhone.setOnClickListener(this);
        llKeFu.setOnClickListener(this);
        llSetting.setOnClickListener(this);
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
      //  int height01 = width;//屏幕高度
        int height01 = width / 2 + 40;//屏幕高度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlHeader.getLayoutParams();
        params.width = width;
       params.height = height01 ;
        mRlHeader.setLayoutParams(params);//设置配置参数
//        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ll01.getLayoutParams();
//        params1.width = width;
//        params1.height = wm.getDefaultDisplay().getHeight() - DensityUtil.dip2px(getActivity(), 50);
//         ll01.setLayoutParams(params1);//设置配置参数
    }


    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llVip:
                toActivity(VipInfoActivity.class);
                break;
            case R.id.llUserInfo:
                bundle = new Bundle();
                bundle.putString("avatarImg", avatar);
                toActivity(MemberActivity.class, bundle);
                break;
            case R.id.llQianDao:
                bundle = new Bundle();
                bundle.putString("Isbindphone", Isbindphone);
                toActivity(SignInActivity.class, bundle);
                break;
            case R.id.llCard:
                toActivity(CardPackageActivity.class);
                break;
            case R.id.llMessage:
                toActivity(MessageActivity.class);
                break;
            case R.id.llBindPhone:
                if ("1".equals(Isbindphone)) {
                    showToast("您已经绑定过手机");
                    return;
                } else {
                    toActivity(BindPhoneActivity.class);
                }
                break;
            case R.id.llKeFu:
                toActivity(CustomerServiceActivity.class);
                break;
            case R.id.llSetting:
                toActivity(SettingActivity.class);
                break;
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_my;
    }


    @Override
    protected void initData(Context mContext) {

    }
}
