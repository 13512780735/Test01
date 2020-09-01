package com.feemoo.fragment.cloud.share;


import android.content.BroadcastReceiver;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.feemoo.R;
import com.feemoo.adapter.HistoryAdapter;
import com.feemoo.adapter.ShareAdapter;
import com.feemoo.base.BaseFragment;
import com.feemoo.fragment.cloud.cloud2.Cloud2Fragment;
import com.feemoo.network.model.BaseResponse;
import com.feemoo.network.model.share.FilesModel;
import com.feemoo.network.model.share.ShareListModel;
import com.feemoo.network.model.share.ShareModel;
import com.feemoo.network.util.DataResultException;
import com.feemoo.network.util.RetrofitUtil;
import com.feemoo.utils.SharedPreferencesUtils;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareListFragment extends BaseFragment implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshRecyclerView mRecycleView;
    private boolean isGetData = false;
    private List<ShareModel> mShareData = new ArrayList<>();
    private ShareAdapter mShareAdapter;
    private int pg = 1;
    private MBroadcastReceiver receiver;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            if (mShareData.size() > 0) {
                mShareData.clear();
                mShareAdapter.notifyDataSetChanged();
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
            if (mShareData.size() > 0) {
                mShareData.clear();
                mShareAdapter.notifyDataSetChanged();
            }
            GetData();
        }
    }

    /**
     * 注册广播
     */
    public void register() {

        receiver = new MBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.share");
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
            String flag = data.getString("flag");
            if ("1".equals(flag)) {
                mShareAdapter.remove(position);
            } else {
                onRefresh();
            }
        }
    }

    public ShareListFragment() {
        // Required empty public constructor
    }

    /**
     * 我的云文件分享列表
     */
    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getsharefiles(token, "", pg, new Subscriber<BaseResponse<ShareListModel>>() {
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
            public void onNext(BaseResponse<ShareListModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    ShareListModel shareListModel = baseResponse.getData();
                    if (shareListModel.getFiles().size() > 0) {
                        List<ShareListModel.FilesBean> filesBeans = shareListModel.getFiles();
                        for (ShareListModel.FilesBean bean : filesBeans) {
                            FilesModel filesModel = new FilesModel();
                            filesModel.setExt(bean.getExt());
                            filesModel.setId(bean.getId());
                            filesModel.setIntime(bean.getIntime());
                            filesModel.setName(bean.getName());
                            filesModel.setSize(bean.getSize());
                            filesModel.setLink(bean.getLink());
                            filesModel.setBasename(bean.getBasename());
                            mShareData.add(filesModel);
                        }
                        mShareAdapter.setNewData(mShareData);
                        mShareAdapter.notifyDataSetChanged();
                    }
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_share_listragment;
    }

    @Override
    protected void initView(View view) {
        SharedPreferencesUtils.put(getActivity(), "share", "");
        register();
        mRecycleView = findView(R.id.mRecycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        View EmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.view_share_recycler_empty, null, false);
        mRecycleView.setEmptyView(EmptyView);
        mRecycleView.setOnListLoadListener(this);
        mRecycleView.setOnRefreshListener(this);
        mShareAdapter = new ShareAdapter(mShareData);
        mRecycleView.setAdapter(mShareAdapter);
        mShareAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListLoad() {
        ++pg;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mShareAdapter.notifyDataSetChanged();
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
                if (mShareData.size() > 0) {
                    mShareData.clear();
                    mShareAdapter.notifyDataSetChanged();
                }
                GetData();
                mShareAdapter.notifyDataSetChanged();
                mRecycleView.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    @Override
    protected void initData(Context mContext) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
        SharedPreferencesUtils.put(getContext(),"cloud","");
    }
}
