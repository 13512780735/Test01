package com.feemoo.activity.cloud;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feemoo.R;
import com.feemoo.adapter.WorkStationAdapter;
import com.feemoo.base.BaseActivity;
import com.feemoo.network.model.BaseResponse;
import com.feemoo.network.model.workStation.FilesModel;
import com.feemoo.network.model.workStation.FolderModel;
import com.feemoo.network.model.workStation.WorkStationListModel;
import com.feemoo.network.model.workStation.workStationModel;
import com.feemoo.network.util.DataResultException;
import com.feemoo.network.util.RetrofitUtil;
import com.feemoo.utils.SharedPreferencesUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

/**
 * 永存空间
 */
public class LiveSpaceActivity extends BaseActivity implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    private SwipeRefreshRecyclerView mRecycleView;
    private WorkStationAdapter mWorkStationAdapter;
    private List<workStationModel> mWorkStationData = new ArrayList<>();
    private MBroadcastReceiver receiver;


    private int pg = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_space);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initUI();
        GetData();
    }

    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getprofiles(token, 1, 0, "", pg, new Subscriber<BaseResponse<WorkStationListModel>>() {
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
            public void onNext(BaseResponse<WorkStationListModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    WorkStationListModel workStationListModel = baseResponse.getData();
                    if (workStationListModel.getFolder().size() > 0) {
                        List<WorkStationListModel.FolderBean> folderBeans = workStationListModel.getFolder();
                        for (WorkStationListModel.FolderBean bean : folderBeans) {
                            FolderModel folderModel = new FolderModel();
                            folderModel.setId(bean.getId());
                            folderModel.setIntime(bean.getIntime());
                            folderModel.setName(bean.getName());
                            mWorkStationData.add(folderModel);
                        }
                    }
                    if (workStationListModel.getFiles().size() > 0) {
                        List<WorkStationListModel.FilesBean> filesBeans = workStationListModel.getFiles();
                        for (WorkStationListModel.FilesBean bean : filesBeans) {
                            FilesModel filesModel = new FilesModel();
                            filesModel.setBasename(bean.getBasename());
                            filesModel.setExt(bean.getExt());
                            filesModel.setId(bean.getId());
                            filesModel.setIntime(bean.getIntime());
                            filesModel.setBasesize(bean.getBasesize());
                            filesModel.setName(bean.getName());
                            filesModel.setSize(bean.getSize());
                            mWorkStationData.add(filesModel);
                        }
                    }
                    mWorkStationAdapter.setNewData(mWorkStationData);
                    mWorkStationAdapter.notifyDataSetChanged();
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    private void initUI() {
        SharedPreferencesUtils.put(mContext, "work", "1");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        register();
        mRecycleView = findView(R.id.mRecycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        View EmptyView = LayoutInflater.from(this).inflate(R.layout.view_livespace_recycler_empty, null, false);
        mRecycleView.setEmptyView(EmptyView);
        mRecycleView.setOnListLoadListener(this);
        mRecycleView.setOnRefreshListener(this);
        mWorkStationAdapter = new WorkStationAdapter(mWorkStationData);
        mRecycleView.setAdapter(mWorkStationAdapter);
        mWorkStationAdapter.notifyDataSetChanged();
    }

    /**
     * 注册广播
     */
    public void register() {

        receiver = new MBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.liveSpace");
        registerReceiver(receiver, filter);
    }

    /**
     * 定义广播
     */
    class MBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            int position = data.getInt("id");
            mWorkStationAdapter.remove(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        SharedPreferencesUtils.put(mContext, "work", "");
    }

    @Override
    public void onListLoad() {
        ++pg;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWorkStationAdapter.notifyDataSetChanged();
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
                if (mWorkStationData.size() > 0) {
                    mWorkStationData.clear();
                    mWorkStationAdapter.notifyDataSetChanged();
                }
                GetData();
                mWorkStationAdapter.notifyDataSetChanged();
                mRecycleView.setRefreshing(false);
            }
        }, 1000);
    }
}
