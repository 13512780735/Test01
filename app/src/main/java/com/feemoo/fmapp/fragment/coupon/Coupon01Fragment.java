package com.feemoo.fmapp.fragment.coupon;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.adapter.CouponListAdapter;
import com.feemoo.fmapp.base.BaseFragment;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.CouponModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class Coupon01Fragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private CouponListAdapter mCouponListAdapter;
    List<CouponModel.CouponBean> mCouponData = new ArrayList<>();
    private SwipeRefreshRecyclerView mRecycleView;
    private boolean isGetData = false;

    public Coupon01Fragment() {
        // Required empty public constructor
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            if (mCouponData.size() > 0) {
                mCouponData.clear();
                mCouponListAdapter.notifyDataSetChanged();
            }
            GetData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isGetData) {
            isGetData = true;
            if (mCouponData.size() > 0) {
                mCouponData.clear();
                mCouponListAdapter.notifyDataSetChanged();
            }
            GetData();
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_coupon01;
    }

    @Override
    protected void initView(View view) {
        mRecycleView = findView(R.id.recycler_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        View EmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.view_coupon_recycler_empty, null, false);
        mRecycleView.setEmptyView(EmptyView);
        mRecycleView.setOnListLoadListener(null);
        mRecycleView.setOnRefreshListener(this);
        mCouponListAdapter = new CouponListAdapter(R.layout.coupon_items, mCouponData);
        mRecycleView.setAdapter(mCouponListAdapter);
        mCouponListAdapter.notifyDataSetChanged();
    }

    private void GetData() {

        LoaddingShow();
        RetrofitUtil.getInstance().coupct(token, new Subscriber<BaseResponse<CouponModel>>() {
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
            public void onNext(BaseResponse<CouponModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    if (baseResponse.getData().getCoupon().size() > 0) {
                        mCouponData = baseResponse.getData().getCoupon();
                        mCouponListAdapter.setNewData(mCouponData);
                        mCouponListAdapter.notifyDataSetChanged();

                    }


                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    @Override
    protected void initData(Context mContext) {

    }

    @Override
    public void onRefresh() {
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCouponData.size() > 0) {
                    mCouponData.clear();
                    mCouponListAdapter.notifyDataSetChanged();
                }
                GetData();
                mCouponListAdapter.notifyDataSetChanged();
                mRecycleView.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }
}
