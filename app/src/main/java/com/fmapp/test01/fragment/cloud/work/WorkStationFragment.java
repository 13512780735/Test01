package com.fmapp.test01.fragment.cloud.work;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.fmapp.test01.R;
import com.fmapp.test01.adapter.WorkStationAdapter;
import com.fmapp.test01.base.BaseFragment;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.workStation.FilesModel;
import com.fmapp.test01.network.model.workStation.FolderModel;
import com.fmapp.test01.network.model.workStation.SpaceModel;
import com.fmapp.test01.network.model.workStation.WorkStationListModel;
import com.fmapp.test01.network.model.workStation.workStationModel;
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
public class WorkStationFragment extends BaseFragment implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshRecyclerView mRecycleView;
    private boolean isGetData = false;
    private WorkStationAdapter mWorkStationAdapter;
    private List<workStationModel> mWorkStationData = new ArrayList<>();
    private MBroadcastReceiver receiver;
    private int pg = 1;
    boolean flag = false;

    public WorkStationFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!isGetData) {
            isGetData = true;
            if (mWorkStationData.size() > 0) {
                mWorkStationData.clear();
                mWorkStationAdapter.notifyDataSetChanged();
            }
            GetData();
        }
    }
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            if (mWorkStationData.size() > 0) {
                mWorkStationData.clear();
                mWorkStationAdapter.notifyDataSetChanged();
            }
            GetData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    /**
     * 操作台数据
     */
    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getprofiles(token, 0, 0, "", pg, new Subscriber<BaseResponse<WorkStationListModel>>() {
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
                if ("1".equals(baseResponse.getStatus()) ) {
                    WorkStationListModel workStationListModel = baseResponse.getData();

                    if (workStationListModel.getSpace().toString().length() > 0) {
                        WorkStationListModel.SpaceBean spaceBeans = workStationListModel.getSpace();
                        SpaceModel spaceModel = new SpaceModel();
                        spaceModel.setTotal(spaceBeans.getTotal());
                        spaceModel.setUsed(spaceBeans.getUsed());
                        if (flag == false) {
                            mWorkStationData.add(spaceModel);
                        }
                    }
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

    public static WorkStationFragment newInstance() {
        return new WorkStationFragment();
    }


    @Override
    protected void initView(View view) {
        register();
        mRecycleView = findView(R.id.mRecycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(),
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
        filter.addAction("android.intent.action.work");
        getActivity().registerReceiver(receiver, filter);
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
                mWorkStationAdapter.remove(position);
            } else {
                onRefresh();
            }
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_work_station;
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
    }


}
