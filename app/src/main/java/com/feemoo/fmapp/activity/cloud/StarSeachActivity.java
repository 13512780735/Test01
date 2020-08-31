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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.adapter.StarAdapter;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.fragment.cloud.star.StarFragment;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.star.FilesModel;
import com.feemoo.fmapp.network.model.star.StarListModel;
import com.feemoo.fmapp.network.model.star.StarModel;
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

public class StarSeachActivity extends BaseActivity implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener{
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

    private List<StarModel> mStarData = new ArrayList<>();
    private StarAdapter mStarAdapter;
    private int pg = 1;
    private MBroadcastReceiver receiver;
    private String keywords="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_seach);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initUI();
    }

    private void initUI() {
        SharedPreferencesUtils.put(mContext,"star","1");
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
                if (mStarData.size() > 0) {
                    mStarData.clear();
                    mStarAdapter.notifyDataSetChanged();
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
        View EmptyView = LayoutInflater.from(mContext).inflate(R.layout.view_star_recycler_empty, null, false);
        mRecycleView.setEmptyView(EmptyView);
        mRecycleView.setOnListLoadListener(this);
        mRecycleView.setOnRefreshListener(this);
        mStarAdapter = new StarAdapter(mStarData);
        mRecycleView.setAdapter(mStarAdapter);
        mStarAdapter.notifyDataSetChanged();
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
     * 获取星标列表数据
     */
    private void GetData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getcltfiles(token, keywords, pg, new Subscriber<BaseResponse<StarListModel>>() {
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
                if ("1".equals(baseResponse.getStatus()) ) {
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
                    showToast( baseResponse.getMsg());
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
                mStarAdapter.notifyDataSetChanged();
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
                if (mStarData.size() > 0) {
                    mStarData.clear();
                    mStarAdapter.notifyDataSetChanged();
                }
                GetData();
                mStarAdapter.notifyDataSetChanged();
                mRecycleView.setRefreshing(false);
            }
        }, 1000);
    }
    /**
     * 注册广播
     */
    public void register() {

        receiver = new MBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.starSearch");
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
            if ("0".equals(flag)) {
                onRefresh();
            } else {
                mStarAdapter.remove(position);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
          unregisterReceiver(receiver);
        }
    }
}
