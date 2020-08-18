package com.fmapp.test01.activity.file.work;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fmapp.test01.R;
import com.fmapp.test01.activity.file.cloud.FilePreviewActivity;
import com.fmapp.test01.adapter.CloudAdapter;
import com.fmapp.test01.adapter.WorkStationAdapter;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.fragment.cloud.work.WorkStationFragment;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.cloud.CloudModel;
import com.fmapp.test01.network.model.workStation.FilesModel;
import com.fmapp.test01.network.model.workStation.FolderModel;
import com.fmapp.test01.network.model.workStation.SpaceModel;
import com.fmapp.test01.network.model.workStation.WorkStationListModel;
import com.fmapp.test01.network.model.workStation.workStationModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class workFilePreviewActivity extends BaseActivity implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshRecyclerView mRecycleView;
    private boolean isGetData = false;
    private WorkStationAdapter mWorkStationAdapter;
    private List<workStationModel> mWorkStationData = new ArrayList<>();
    private MBroadcastReceiver receiver;
    private int pg = 1;
    boolean flag = false;
    String id;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_file_preview);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        register();
        initView();
        GetData();
    }

    /**
     * 操作台数据
     */
    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getprofiles(token, 0, Integer.parseInt(id), "", pg, new Subscriber<BaseResponse<WorkStationListModel>>() {
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
                Log.d("数据Work", baseResponse.getMsg());
                if ("1".equals(baseResponse.getStatus())) {
                    WorkStationListModel workStationListModel = baseResponse.getData();

                  /*  if (workStationListModel.getSpace().toString().length() > 0) {
                        WorkStationListModel.SpaceBean spaceBeans = workStationListModel.getSpace();
                        SpaceModel spaceModel = new SpaceModel();
                        spaceModel.setTotal(spaceBeans.getTotal());
                        spaceModel.setUsed(spaceBeans.getUsed());
                        if (flag == false) {
                            mWorkStationData.add(spaceModel);
                        }
                    }*/
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

    public void initView() {
        SharedPreferencesUtils.put(mContext,"work","2");
        setBackView();
        setTitle(name);

        mRecycleView = findView(R.id.recycler_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
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
        filter.addAction("android.intent.action.workFile");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onListLoad() {
        ++pg;
        flag = true;
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

//    @Override
//    public void onResume() {
//        super.onResume();
//        onRefresh();
//    }

    @Override
    public void onRefresh() {
        pg = 1;
        flag = false;
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

    /**
     * 定义广播
     */
    class MBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            String flag = data.getString("flag");
            int position = data.getInt("id");
            if ("0".equals(flag)) {
                onRefresh();
            } else {
                onRefresh();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
        SharedPreferencesUtils.put(mContext,"work","");
    }
}
