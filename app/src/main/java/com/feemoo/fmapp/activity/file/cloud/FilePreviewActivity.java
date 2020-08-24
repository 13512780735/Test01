package com.feemoo.fmapp.activity.file.cloud;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.adapter.CloudAdapter;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.FilesListModel;
import com.feemoo.fmapp.network.model.cloud.CloudModel;
import com.feemoo.fmapp.network.model.cloud.FilesModel;
import com.feemoo.fmapp.network.model.cloud.FolderModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class FilePreviewActivity extends BaseActivity implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshRecyclerView mRecycleView;
    private List<CloudModel> mCloudData = new ArrayList<>();
    private CloudAdapter mCloudAdapter;
    private int pg = 1;
    private String name;
    private String id;
    private MBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_preview);
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        SharedPreferencesUtils.put(mContext, "cloud", "1");
        initView();
        GetData();
    }

    private void initView() {
        setBackView();
        setTitle(name);
        register();
        mRecycleView = findView(R.id.recycler_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        View EmptyView = LayoutInflater.from(mContext).inflate(R.layout.view_cloud_recycler_empty, null, false);
        mRecycleView.setEmptyView(EmptyView);
        mRecycleView.setOnListLoadListener(this);
        mRecycleView.setOnRefreshListener(this);
        mCloudAdapter = new CloudAdapter(mCloudData);
        mRecycleView.setAdapter(mCloudAdapter);
        mCloudAdapter.notifyDataSetChanged();
    }

    /**
     * 加载更多
     */
    @Override
    public void onListLoad() {
        ++pg;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCloudAdapter.notifyDataSetChanged();
                mRecycleView.setLoading(false);
                if (pg == 1) {
                    mRecycleView.setLoadCompleted(true);
                } else GetData();
                mRecycleView.setLoadCompleted(true);
            }
        }, 2000);
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        onRefresh();
//    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        pg = 1;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCloudData.size() > 0) {
                    mCloudData.clear();
                    mCloudAdapter.notifyDataSetChanged();
                }
                GetData();
                mCloudAdapter.notifyDataSetChanged();
                mRecycleView.setRefreshing(false);
            }
        }, 1000);
    }

    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getfiles(token, Integer.parseInt(id), "", pg, new Subscriber<BaseResponse<FilesListModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoaddingDismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    Looper.prepare();
                    showToast( resultException.getMsg());
                    Looper.loop();
                }
            }

            @Override
            public void onNext(BaseResponse<FilesListModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    FilesListModel filesListModel = baseResponse.getData();
                    if (filesListModel.getFolder().size() > 0) {
                        List<FilesListModel.FolderBean> folderBeans = filesListModel.getFolder();
                        for (FilesListModel.FolderBean bean : folderBeans) {
                            FolderModel folderModel = new FolderModel();
                            folderModel.setId(bean.getId());
                            folderModel.setIntime(bean.getIntime());
                            folderModel.setName(bean.getName());
                            mCloudData.add(folderModel);
                        }
                    }
                    if (filesListModel.getFiles().size() > 0) {
                        List<FilesListModel.FilesBean> filesBeans = filesListModel.getFiles();
                        for (FilesListModel.FilesBean bean : filesBeans) {
                            FilesModel filesModel = new FilesModel();
                            filesModel.setBasename(bean.getBasename());
                            filesModel.setExt(bean.getExt());
                            filesModel.setId(bean.getId());
                            filesModel.setIntime(bean.getIntime());
                            filesModel.setLink(bean.getLink());
                            filesModel.setName(bean.getName());
                            filesModel.setSize(bean.getSize());
                            mCloudData.add(filesModel);
                        }
                    }
                    mCloudAdapter.setNewData(mCloudData);
                    mCloudAdapter.notifyDataSetChanged();
                } else {
                    Looper.prepare();
                    showToast( baseResponse.getMsg());
                    Looper.loop();
                }

            }
        });
    }

    /**
     * 注册广播
     */
    public void register() {

        receiver = new MBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.cloudFile");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        SharedPreferencesUtils.put(mContext, "cloud", "");
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
                mCloudAdapter.remove(position);
            } else {
                onRefresh();
            }
        }
    }
}
