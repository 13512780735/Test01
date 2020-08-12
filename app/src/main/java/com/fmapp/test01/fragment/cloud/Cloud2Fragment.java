package com.fmapp.test01.fragment.cloud;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.fmapp.test01.adapter.CloudAdapter;
import com.fmapp.test01.base.BaseFragment;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.FilesListModel;
import com.fmapp.test01.network.model.cloud.CloudModel;
import com.fmapp.test01.network.model.cloud.FilesModel;
import com.fmapp.test01.network.model.cloud.FolderModel;
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
public class Cloud2Fragment extends BaseFragment implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshRecyclerView mRecycleView;
    private boolean isGetData = false;
    private List<CloudModel> mCloudData = new ArrayList<>();
    private CloudAdapter mCloudAdapter;
    private MBroadcastReceiver receiver;
    private int pg = 1;

    public Cloud2Fragment() {
        // Required empty public constructor
    }


    public static Cloud2Fragment newInstance() {
        return new Cloud2Fragment();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Log.d("执行", "onCreateAnimation");
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            if (mCloudData.size() > 0) {
                mCloudData.clear();
                mCloudAdapter.notifyDataSetChanged();
            }
            GetData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getfiles(token, 0, "", pg, new Subscriber<BaseResponse<FilesListModel>>() {
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
                Log.d("数据Cloud",baseResponse.getMsg());
                if ("1".equals(baseResponse.getStatus()) ) {
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
    protected void initView(View view) {
        register();
        mRecycleView = findViewById(R.id.recycler_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        View EmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.view_cloud_recycler_empty, null, false);
        mRecycleView.setEmptyView(EmptyView);
        mRecycleView.setOnListLoadListener(this);
        mRecycleView.setOnRefreshListener(this);
        mCloudAdapter = new CloudAdapter(mCloudData);
        mRecycleView.setAdapter(mCloudAdapter);
        mCloudAdapter.notifyDataSetChanged();

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
    protected int initLayout() {
        return R.layout.fragment_cloud2;
    }


    @Override
    protected void initData(Context mContext) {
      //  GetData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }
}
