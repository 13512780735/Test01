package com.fmapp.test01.fragment.main;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.fmapp.test01.R;
import com.fmapp.test01.activity.MyInfo.MemberActivity;
import com.fmapp.test01.activity.MyInfo.SettingActivity;
import com.fmapp.test01.base.BaseFragment;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.MemberModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.DensityUtil;
import com.fmapp.test01.utils.ImageLoaderUtils;
import com.fmapp.test01.widght.CircleImageView;

import rx.Subscriber;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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
                if ("1".equals(baseResponse.getStatus()) ) {
                    refreshUI(baseResponse.getData());
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });

    }

    private void refreshUI(MemberModel data) {
        mTitle.setText(data.getUsername());
        mTvVip.setText(data.getSlevel());
        mTvEndTime.setText(data.getEndtime());
        ImageLoaderUtils.getInstance(getActivity()).displayImage(data.getLogo(), ivHeader);
        //  Glide.with(getActivity()).load(data.getLogo()).transition(withCrossFade()).into(ivHeader);
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
        int height01 = width;//屏幕高度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlHeader.getLayoutParams();
        params.width = width;
        params.height = height01 / 3 * 2;
        mRlHeader.setLayoutParams(params);//设置配置参数
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ll01.getLayoutParams();
        params1.width = width;
        params1.height = wm.getDefaultDisplay().getHeight() - DensityUtil.dip2px(getActivity(), 50);
        // ll01.setLayoutParams(params1);//设置配置参数
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

                break;
            case R.id.llUserInfo:
                toActivity(MemberActivity.class);
                break;
            case R.id.llQianDao:
                break;
            case R.id.llCard:
                break;
            case R.id.llMessage:
                break;
            case R.id.llBindPhone:
                break;
            case R.id.llKeFu:
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
