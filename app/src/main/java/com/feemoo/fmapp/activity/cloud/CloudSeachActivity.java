package com.feemoo.fmapp.activity.cloud;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class CloudSeachActivity extends BaseActivity implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {
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
    private List<CloudModel> mCloudData = new ArrayList<>();
    private CloudAdapter mCloudAdapter;
    private MBroadcastReceiver receiver;
    private int pg = 1;
    String keywords = "";
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_seach);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getString("flag");
        }
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initUI();
    }

    private void initUI() {
        SharedPreferencesUtils.put(mContext,"cloud","2");
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
                if (mCloudData.size() > 0) {
                    mCloudData.clear();
                    mCloudAdapter.notifyDataSetChanged();
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
        register();
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
     * 注册广播
     */
    public void register() {

        receiver = new MBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.cloudSeach");
        registerReceiver(receiver, filter);
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

    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getfiles(token, 0, keywords, pg, new Subscriber<BaseResponse<FilesListModel>>() {
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
                    showToast(baseResponse.getMsg());
                }

            }
        });
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
            String flag = data.getString("flag");
            if ("1".equals(flag)) {
                mCloudAdapter.remove(position);
            } else {
                onRefresh();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        SharedPreferencesUtils.put(mContext, "cloud", "");
    }
}
