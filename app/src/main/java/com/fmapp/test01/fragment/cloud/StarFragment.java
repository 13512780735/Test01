package com.fmapp.test01.fragment.cloud;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.animation.Animation;

import com.fmapp.test01.R;
import com.fmapp.test01.adapter.StarAdapter;
import com.fmapp.test01.base.BaseFragment;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.star.FilesModel;
import com.fmapp.test01.network.model.star.StarListModel;
import com.fmapp.test01.network.model.star.StarModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class StarFragment extends BaseFragment {

    private RecyclerView mRecycleView;
    private boolean isGetData = false;
    private List<StarModel> mStarData = new ArrayList<>();
    private StarAdapter mStarAdapter;

    public StarFragment() {
        // Required empty public constructor
    }

    public static StarFragment newInstance() {
        return new StarFragment();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
            if (mStarData.size() > 0) {
                mStarData.clear();
            }
            GetData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    /**
     * 获取星标列表数据
     */
    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getcltfiles(token, "", 1, new Subscriber<BaseResponse<StarListModel>>() {
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
            public void onNext(BaseResponse<StarListModel> baseResponse) {
                LoaddingDismiss();
                if (baseResponse.getStatus() == 1) {
                    StarListModel starListModel = baseResponse.getData();
                    if (starListModel.getFiles().size() > 0) {
                        List<StarListModel.FilesBean> filesBeans = starListModel.getFiles();
                        for (StarListModel.FilesBean bean : filesBeans) {
                            FilesModel filesModel = new FilesModel();
                            filesModel.setBasename(bean.getBasename());
                            filesModel.setExt(bean.getExt());
                            filesModel.setFid(bean.getFid());
                            filesModel.setIn_time(bean.getIn_time());
                            filesModel.setId(bean.getId());
                            filesModel.setIntime(bean.getIntime());
                            filesModel.setName(bean.getName());
                            filesModel.setSize(bean.getSize());
                            filesModel.setFshort(bean.getFshort());
                            mStarData.add(filesModel);
                        }
                    }
                    mStarAdapter.setNewData(mStarData);
                    mStarAdapter.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
        initUI();
    }

    private void initUI() {
        mRecycleView = findViewById(R.id.recycler_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStarAdapter = new StarAdapter(mStarData);
        mRecycleView.setAdapter(mStarAdapter);
        mStarAdapter.notifyDataSetChanged();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_star;
    }

    @Override
    protected void lazyLoad() {

    }

}
