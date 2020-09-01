package com.feemoo.activity.cloud;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.feemoo.R;
import com.feemoo.adapter.CloudAdapter;
import com.feemoo.adapter.ShareAdapter;
import com.feemoo.base.BaseActivity;
import com.feemoo.fragment.cloud.share.ShareListFragment;
import com.feemoo.network.model.BaseResponse;
import com.feemoo.network.model.cloud.CloudModel;
import com.feemoo.network.model.share.FilesModel;
import com.feemoo.network.model.share.ShareListModel;
import com.feemoo.network.model.share.ShareModel;
import com.feemoo.network.util.DataResultException;
import com.feemoo.network.util.RetrofitUtil;
import com.feemoo.utils.SharedPreferencesUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class ShareSearchActivity extends BaseActivity implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {
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
    private List<ShareModel> mShareData = new ArrayList<>();
    private ShareAdapter mShareAdapter;
    private int pg = 1;
    String keywords = "";
    private String flag;
    private MBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_search);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initUI();
    }

    private void initUI() {
        SharedPreferencesUtils.put(mContext, "share", "1");
        register();
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
                if (mShareData.size() > 0) {
                    mShareData.clear();
                    mShareAdapter.notifyDataSetChanged();
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
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        View EmptyView = LayoutInflater.from(mContext).inflate(R.layout.view_share_recycler_empty, null, false);
        mRecycleView.setEmptyView(EmptyView);
        mRecycleView.setOnListLoadListener(this);
        mRecycleView.setOnRefreshListener(this);
        mShareAdapter = new ShareAdapter(mShareData);
        mRecycleView.setAdapter(mShareAdapter);
        mShareAdapter.notifyDataSetChanged();
    }
    /**
     * 注册广播
     */
    public void register() {

        receiver = new MBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.shareSearch");
        registerReceiver(receiver, filter);
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
                mShareAdapter.remove(position);
            } else {
                onRefresh();
            }
        }
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
     * 我的云文件分享列表
     */
    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getsharefiles(token, keywords, pg, new Subscriber<BaseResponse<ShareListModel>>() {
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
            public void onNext(BaseResponse<ShareListModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    ShareListModel shareListModel = baseResponse.getData();
                    if (shareListModel.getFiles().size() > 0) {
                        List<ShareListModel.FilesBean> filesBeans = shareListModel.getFiles();
                        for (ShareListModel.FilesBean bean : filesBeans) {
                            FilesModel filesModel = new FilesModel();
                            filesModel.setExt(bean.getExt());
                            filesModel.setId(bean.getId());
                            filesModel.setIntime(bean.getIntime());
                            filesModel.setName(bean.getName());
                            filesModel.setSize(bean.getSize());
                            filesModel.setLink(bean.getLink());
                            filesModel.setBasename(bean.getBasename());
                            mShareData.add(filesModel);
                        }
                        mShareAdapter.setNewData(mShareData);
                        mShareAdapter.notifyDataSetChanged();
                    }
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    @Override
    public void onListLoad() {
        ++pg;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mShareAdapter.notifyDataSetChanged();
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
                if (mShareData.size() > 0) {
                    mShareData.clear();
                    mShareAdapter.notifyDataSetChanged();
                }
                GetData();
                mShareAdapter.notifyDataSetChanged();
                mRecycleView.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
           unregisterReceiver(receiver);
        }
        SharedPreferencesUtils.put(mContext,"cloud","");
    }
}
