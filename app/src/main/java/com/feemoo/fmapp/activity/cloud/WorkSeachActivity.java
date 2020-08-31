package com.feemoo.fmapp.activity.cloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.adapter.CloudAdapter;
import com.feemoo.fmapp.adapter.WorkStationAdapter;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.fragment.cloud.work.WorkStationFragment;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.cloud.CloudModel;
import com.feemoo.fmapp.network.model.workStation.FilesModel;
import com.feemoo.fmapp.network.model.workStation.FolderModel;
import com.feemoo.fmapp.network.model.workStation.SpaceModel;
import com.feemoo.fmapp.network.model.workStation.WorkStationListModel;
import com.feemoo.fmapp.network.model.workStation.workStationModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class WorkSeachActivity extends BaseActivity implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view)
    SwipeRefreshRecyclerView mRecycleView;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    private WorkStationAdapter mWorkStationAdapter;
    private List<workStationModel> mWorkStationData = new ArrayList<>();
    private MBroadcastReceiver receiver;
    private int pg = 1;
    String keywords = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_seach);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initUI();
    }

    private void initUI() {
        SharedPreferencesUtils.put(mContext,"work","3");
        register();
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        mRecycleView.setOnListLoadListener(this);
        mRecycleView.setOnRefreshListener(this);
        mWorkStationAdapter = new WorkStationAdapter(mWorkStationData);
        mRecycleView.setAdapter(mWorkStationAdapter);
        mWorkStationAdapter.notifyDataSetChanged();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint("搜索");
        mSearchView.setImeOptions(3);
        setUnderLinetransparent(mSearchView);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  LogUtil.e(MainActivity.class,"=====query="+query);
                keywords = query;
                if (mWorkStationData.size() > 0) {
                    mWorkStationData.clear();
                    mWorkStationAdapter.notifyDataSetChanged();
                }
                GetData();
                mSearchView.clearFocus();
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                // LogUtil.e(MainActivity.class,"=====newText="+newText);
                keywords = newText;
                return false;
            }
        });
    }

    /**
     * 设置SearchView下划线透明
     **/
    private void setUnderLinetransparent(SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();
            // mSearchPlate是SearchView父布局的名字
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 操作台数据
     */
    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getprofiles(token, 0, 0, keywords, pg, new Subscriber<BaseResponse<WorkStationListModel>>() {
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

    /**
     * 注册广播
     */
    public void register() {

        receiver = new MBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.workSeach");
        registerReceiver(receiver, filter);
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

}
