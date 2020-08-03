package com.fmapp.test01.fragment.cloud;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.animation.Animation;

import com.fmapp.test01.R;
import com.fmapp.test01.adapter.HistoryAdapter;
import com.fmapp.test01.base.BaseFragment;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.history.FilesModel;
import com.fmapp.test01.network.model.history.HistoryListModel;
import com.fmapp.test01.network.model.history.HistoryModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadHistoryFragment extends BaseFragment {

    private RecyclerView mRecycleView;
    private boolean isGetData = false;
    private List<HistoryModel> mHisData = new ArrayList<>();
    private HistoryAdapter mHistoryAdapter;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            if (mHisData.size() > 0) {
                mHisData.clear();
            }
            GetData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    /**
     * 获取下载历史列表数据
     */
    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().gethisfiles(token, 1, new Subscriber<BaseResponse<HistoryListModel>>() {
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
                if (baseResponse.getStatus() == 1) {
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

    public DownloadHistoryFragment() {
        // Required empty public constructor
    }

    public static DownloadHistoryFragment newInstance() {
        return new DownloadHistoryFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        initUI();
    }

    private void initUI() {
        mRecycleView = findViewById(R.id.recycler_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHistoryAdapter = new HistoryAdapter(mHisData);
        mRecycleView.setAdapter(mHistoryAdapter);
        mHistoryAdapter.notifyDataSetChanged();
    }


    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_download_history;
    }

    @Override
    protected void lazyLoad() {

    }

}
