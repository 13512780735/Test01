package com.fmapp.test01.activity.file;


import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmapp.test01.R;
import com.fmapp.test01.activity.login.Register02Activity;
import com.fmapp.test01.adapter.CloudAdapter;
import com.fmapp.test01.adapter.OnlineZipAdapter;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.network.Appconst.AppConst;
import com.fmapp.test01.network.model.OnlineZipModel;
import com.fmapp.test01.network.model.cloud.CloudModel;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ZipPreviewActivity extends BaseActivity {
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
        setBackView();
        setTitle(name);
        mRecycleView = findView(R.id.recycler_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        mOnlineZipAdapter = new OnlineZipAdapter(R.layout.zip_online_item, mOnlineDatas);
        mRecycleView.setAdapter(mOnlineZipAdapter);
        mOnlineZipAdapter.notifyDataSetChanged();
    }


}
