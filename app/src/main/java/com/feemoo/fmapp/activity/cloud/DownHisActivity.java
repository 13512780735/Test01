package com.feemoo.fmapp.activity.cloud;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.adapter.HistoryAdapter;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.history.FilesModel;
import com.feemoo.fmapp.network.model.history.HistoryListModel;
import com.feemoo.fmapp.network.model.history.HistoryModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class DownHisActivity extends BaseActivity implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.mRecycleView)
    SwipeRefreshRecyclerView mRecycleView;
    private List<HistoryModel> mHisData = new ArrayList<>();
    private HistoryAdapter mHistoryAdapter;
    private int pg = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_his);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initUI();
        GetData();
    }

    private void initUI() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mRecycleView = findView(R.id.mRecycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        View EmptyView = LayoutInflater.from(mContext).inflate(R.layout.view_downhis_recycler_empty, null, false);
        mRecycleView.setEmptyView(EmptyView);
        mRecycleView.setOnListLoadListener(this);
        mRecycleView.setOnRefreshListener(this);
        mHistoryAdapter = new HistoryAdapter(mHisData);
        mRecycleView.setAdapter(mHistoryAdapter);
        mHistoryAdapter.notifyDataSetChanged();
    }

    /**
     * 获取下载历史列表数据
     */
    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().gethisfiles(token, pg, new Subscriber<BaseResponse<HistoryListModel>>() {
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
            public void onNext(BaseResponse<HistoryListModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    HistoryListModel historyListModel = baseResponse.getData();
                    if (historyListModel.getFiles().size() > 0) {
                        List<HistoryListModel.FilesBean> filesBeans = historyListModel.getFiles();
                        for (HistoryListModel.FilesBean bean : filesBeans) {
                            FilesModel filesModel = new FilesModel();
                            filesModel.setExt(bean.getExt());
                            filesModel.setFid(bean.getFid());
                            filesModel.setId(bean.getId());
                            filesModel.setIntime(bean.getIntime());
                            filesModel.setName(bean.getName());
                            filesModel.setSize(bean.getSize());
                            mHisData.add(filesModel);
                        }
                        mHistoryAdapter.setNewData(mHisData);
                        mHistoryAdapter.notifyDataSetChanged();
                    }
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    @Override
    public void onListLoad() {
        ++pg;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHistoryAdapter.notifyDataSetChanged();
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
                if (mHisData.size() > 0) {
                    mHisData.clear();
                    mHistoryAdapter.notifyDataSetChanged();
                }
                GetData();
                mHistoryAdapter.notifyDataSetChanged();
                mRecycleView.setRefreshing(false);
            }
        }, 1000);
    }
}
