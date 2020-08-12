package com.fmapp.test01.fragment.cloud;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmapp.test01.R;
import com.fmapp.test01.adapter.StarAdapter;
import com.fmapp.test01.base.BaseFragment;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.star.FilesModel;
import com.fmapp.test01.network.model.star.StarListModel;
import com.fmapp.test01.network.model.star.StarModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class StarFragment extends BaseFragment implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshRecyclerView mRecycleView;
    private boolean isGetData = false;
    private List<StarModel> mStarData = new ArrayList<>();
    private StarAdapter mStarAdapter;
    private int pg = 1;
    private MBroadcastReceiver receiver;

    public StarFragment() {
    }

    public static StarFragment newInstance() {
        return new StarFragment();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            if (mStarData.size() > 0) {
                mStarData.clear();
                mStarAdapter.notifyDataSetChanged();
            }
            GetData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    /**
     * 获取星标列表数据
     */
    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getcltfiles(token, "", pg, new Subscriber<BaseResponse<StarListModel>>() {
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
            public void onNext(BaseResponse<StarListModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus()) ) {
                    StarListModel starListModel = baseResponse.getData();
                    if (starListModel.getFiles().size() > 0) {
                        List<StarListModel.FilesBean> filesBeans = starListModel.getFiles();
                        for (StarListModel.FilesBean bean : filesBeans) {
                            FilesModel filesModel = new FilesModel();
                            filesModel.setBasename(bean.getBasename());
                            filesModel.setExt(bean.getExt());
                            filesModel.setFid(bean.getFid());
                            filesModel.setIn_time(bean.getIn_time());
                            filesModel.setId(bean.getId());
                            filesModel.setIntime(bean.getIntime());
                            filesModel.setName(bean.getName());
                            filesModel.setSize(bean.getSize());
                            filesModel.setFshort(bean.getFshort());
                            mStarData.add(filesModel);
                        }
                    }
                    mStarAdapter.setNewData(mStarData);
                    mStarAdapter.notifyDataSetChanged();
                } else {
                    showToast(baseResponse.getMsg());
                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    @Override
    protected void initView(View view) {
        register();
        mRecycleView = findView(R.id.mRecycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        View EmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.view_star_recycler_empty, null, false);
        mRecycleView.setEmptyView(EmptyView);
        mRecycleView.setOnListLoadListener(this);
        mRecycleView.setOnRefreshListener(this);
        mStarAdapter = new StarAdapter(mStarData);
        mRecycleView.setAdapter(mStarAdapter);
        mStarAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListLoad() {
        ++pg;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStarAdapter.notifyDataSetChanged();
                mRecycleView.setLoading(false);
                if (pg == 1) {
                    mRecycleView.setLoadCompleted(true);
                } else GetData();
                mRecycleView.setLoadCompleted(true);
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        pg = 1;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mStarData.size() > 0) {
                    mStarData.clear();
                    mStarAdapter.notifyDataSetChanged();
                }
                GetData();
                mStarAdapter.notifyDataSetChanged();
                mRecycleView.setRefreshing(false);
            }
        }, 1000);
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        onRefresh();
//    }

    /**
     * 注册广播
     */
    public void register() {

        receiver = new MBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.star");
        getActivity().registerReceiver(receiver, filter);
    }

    /**
     * 定义广播
     */
    class MBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            int position = data.getInt("id");
            mStarAdapter.remove(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_star;
    }


    @Override
    protected void initData(Context mContext) {

    }
}
