package com.feemoo.activity.file;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feemoo.R;
import com.feemoo.adapter.OnlineZipAdapter;
import com.feemoo.base.BaseActivity;
import com.feemoo.network.model.OnlineZipModel;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ZipPreviewActivity extends BaseActivity {
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.tv_title)
    TextView mTitle;
    private RecyclerView mRecycleView;
    private List<OnlineZipModel> mOnlineDatas = new ArrayList<>();
    private OnlineZipAdapter mOnlineZipAdapter;
    private int pg = 1;
    private String url;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_preview);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        initView();
        GetData();
    }

    private void GetData() {
        //  String url = AppConst.BASE_URL + "user/checkregphone";
        loaddingDialog.show();
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request
                .Builder()
                .addHeader("token", token)
                .url(url)//要访问的链接
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoaddingDismiss();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                LoaddingDismiss();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if (status == 1) {
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            OnlineZipModel onlineZipModel = new OnlineZipModel();
                            JSONObject object1 = array.optJSONObject(i);
                            onlineZipModel.setSize(object1.optString("size"));
                            onlineZipModel.setName(object1.optString("name"));
                            onlineZipModel.setExt(object1.optString("ext"));
                            mOnlineDatas.add(onlineZipModel);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mOnlineZipAdapter.setNewData(mOnlineDatas);
                                mOnlineZipAdapter.notifyDataSetChanged();
                            }
                        });

                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        mTitle.setText(name);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mRecycleView = findView(R.id.recycler_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        mOnlineZipAdapter = new OnlineZipAdapter(R.layout.zip_online_item, mOnlineDatas);
        mRecycleView.setAdapter(mOnlineZipAdapter);
        mOnlineZipAdapter.notifyDataSetChanged();
    }


}
