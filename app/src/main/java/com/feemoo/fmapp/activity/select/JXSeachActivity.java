package com.feemoo.fmapp.activity.select;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.adapter.SelectListAdapter;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.Appconst.AppConst;
import com.feemoo.fmapp.network.model.select.JXHomeModel;
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JXSeachActivity extends BaseActivity implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view)
    SwipeRefreshRecyclerView mRecycleView;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    List<JXHomeModel> jxHomeModelList = new ArrayList<>();
    String keywords = "";
    private SelectListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jxseach);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initUI();
    }

    private void initUI() {
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
                if (jxHomeModelList.size() > 0) {
                    jxHomeModelList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                GetHomeData2();
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

        mRecycleView.setLayoutManager(new GridLayoutManager(mContext,
                2));
        mRecycleView.setOnListLoadListener(this);

        mRecycleView.setOnRefreshListener(this);
        mAdapter = new SelectListAdapter(R.layout.select_list_item, jxHomeModelList);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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

    int pg = 1;

    /**
     * 加载更多或者点击文档类
     *
     * @param
     */
    private void GetHomeData2() {
        LoaddingShow();
        String url = AppConst.BASE_URL + "choice/getjxfile";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("pg", String.valueOf(pg))
                .add("keywords", keywords)
                .build();
        Request request = new Request
                .Builder()
                .addHeader("token", token)
                .url(url)//要访问的链接
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoaddingDismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LoaddingDismiss();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if ("1".equals(String.valueOf(status))) {
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.optJSONObject(i);
                            JXHomeModel jxHomeModel = new JXHomeModel();
                            jxHomeModel.setId(object1.optString("id"));
                            jxHomeModel.setImg(object1.optString("img"));
                            jxHomeModel.setName(object1.optString("name"));
                            jxHomeModel.setTag(object1.optString("tag"));
                            jxHomeModelList.add(jxHomeModel);

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setNewData(jxHomeModelList);
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    } else {
                        Looper.prepare();
                        showToast(msg);
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        pg = 1;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (jxHomeModelList.size() > 0) {
                    jxHomeModelList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                GetHomeData2();
                mAdapter.notifyDataSetChanged();
                mRecycleView.setRefreshing(false);
            }
        }, 1000);


    }


    @Override
    public void onListLoad() {
        ++pg;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mRecycleView.setLoading(false);
                if (pg == 1) {
                    mRecycleView.setLoadCompleted(true);
                } else GetHomeData2();
                mRecycleView.setLoadCompleted(true);
            }
        }, 2000);
    }
}
