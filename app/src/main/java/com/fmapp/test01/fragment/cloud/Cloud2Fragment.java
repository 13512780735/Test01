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
import com.fmapp.test01.adapter.CloudAdapter;
import com.fmapp.test01.base.BaseFragment;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.FilesListModel;
import com.fmapp.test01.network.model.cloud.CloudModel;
import com.fmapp.test01.network.model.cloud.FilesModel;
import com.fmapp.test01.network.model.cloud.FolderModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cloud2Fragment extends BaseFragment {


    private RecyclerView mRecycleView;
    private boolean isGetData = false;
    private List<CloudModel> mCloudData = new ArrayList<>();
    private CloudAdapter mCloudAdapter;
    private MBroadcastReceiver receiver;

    public Cloud2Fragment() {
        // Required empty public constructor
    }


    public static Cloud2Fragment newInstance() {
        return new Cloud2Fragment();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            if (mCloudData.size() > 0) {
                mCloudData.clear();
            }
            GetData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getfiles(token, 0, "", 1, new Subscriber<BaseResponse<FilesListModel>>() {
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
            public void onNext(BaseResponse<FilesListModel> baseResponse) {
                LoaddingDismiss();
                if (baseResponse.getStatus() == 1) {
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
    protected int setContentView() {
        return R.layout.fragment_cloud2;
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
        mCloudAdapter = new CloudAdapter(mCloudData);
        mRecycleView.setAdapter(mCloudAdapter);
        mCloudAdapter.notifyDataSetChanged();

    }

    @Override
    protected void lazyLoad() {

    }

    /**
     * 注册广播
     */
    public void register() {

        receiver = new MBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.cloud");
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
            mCloudAdapter.remove(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }
}
