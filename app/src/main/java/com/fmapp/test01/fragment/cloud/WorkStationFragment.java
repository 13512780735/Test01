package com.fmapp.test01.fragment.cloud;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkStationFragment extends BaseFragment {

    private RecyclerView mRecycleView;
    private boolean isGetData = false;
    private WorkStationAdapter mWorkStationAdapter;
    private List<workStationModel> mWorkStationData = new ArrayList<>();
    private MBroadcastReceiver receiver;

    public WorkStationFragment() {
        // Required empty public constructor
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            if (mWorkStationData.size() > 0) {
                mWorkStationData.clear();
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
        RetrofitUtil.getInstance().getprofiles(token, 0, 0, "", 1, new Subscriber<BaseResponse<WorkStationListModel>>() {
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
                if (baseResponse.getStatus() == 1) {
                    WorkStationListModel workStationListModel = baseResponse.getData();

                    if (workStationListModel.getSpace().toString().length() > 0) {
                        WorkStationListModel.SpaceBean spaceBeans = workStationListModel.getSpace();
                        SpaceModel spaceModel = new SpaceModel();
                        spaceModel.setTotal(spaceBeans.getTotal());
                        spaceModel.setUsed(spaceBeans.getUsed());
                        mWorkStationData.add(spaceModel);
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
    protected int setContentView() {
        return R.layout.fragment_work_station;
    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
    }

    private void initUI() {
        register();
        mRecycleView = findViewById(R.id.recycler_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            getActivity().unregisterReceiver(receiver);
        }
    }

    @Override
    protected void lazyLoad() {

    }

}
