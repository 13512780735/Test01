package com.feemoo.fmapp.fragment.cloud.cloud2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.adapter.CLoudMoveFileAdpter;
import com.feemoo.fmapp.network.Appconst.AppConst;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.MoveFoldModel;
import com.feemoo.fmapp.network.model.cloud.FilesModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.LoaddingDialog;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.feemoo.fmapp.utils.ToastUtil;

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
import rx.Subscriber;

public class showMoveBottomDialog {
    private View view;
    private LoaddingDialog loaddingDialog;
    private String cloudFlag;
    private TextView tv_location;
    private RecyclerView mRecycleView;
    //type 0 文件类 1，在线压缩包
    private List<MoveFoldModel> mMoveData = new ArrayList<>();
    private CLoudMoveFileAdpter mAdapter;
    private String id;
    private String fileid;

    private String folderid;
    private TextView tv_right;
    private Dialog dialog;
    private String flag;
    private String location;

    public void BottomDialog(Context context, FilesModel data, int positionss) {
        loaddingDialog = new LoaddingDialog(context);
        //1、使用Dialog、设置style
        dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        view = View.inflate(context, R.layout.move_dialog, null);
        dialog.setContentView(view);
        fileid = data.getId();
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
        cloudFlag = SharedPreferencesUtils.getString(context, "cloud");
        tv_location = view.findViewById(R.id.tv_location);
        tv_right = view.findViewById(R.id.tv_right);
        location = "当前位置";
        tv_location.setText(location);
        mRecycleView = view.findViewById(R.id.mRecycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new CLoudMoveFileAdpter(R.layout.cloud_folder_item, mMoveData);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        dialog.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        id = "0";
        flag = "0";
        initData(context, id);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    folderid = mMoveData.get(position).getId();
                    location = location + ">" + mMoveData.get(position).getName();
                    tv_location.setText(location);
                    if (mMoveData.size() > 0) {
                        mMoveData.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                    initData(context, folderid);
            }
        });

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMove(context, fileid, folderid, positionss);
            }
        });
    }

    /**
     * 移动文件
     *
     * @param context
     * @param fileid
     * @param folderid
     * @param positionss
     */
    private void toMove(Context context, String fileid, String folderid, int positionss) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().movefile(SharedPreferencesUtils.getString(context, "token"), fileid, folderid, new Subscriber<BaseResponse<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loaddingDialog.dismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    showToast(context, resultException.getMsg());
                }
            }

            @Override
            public void onNext(BaseResponse<String> baseResponse) {
                loaddingDialog.dismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    showToast(context, baseResponse.getMsg());
                    if ("1".equals(cloudFlag)) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.cloudFile");
                        intent.putExtra("id", positionss);
                        intent.putExtra("flag", "1");
                        context.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.cloud");
                        intent.putExtra("id", positionss);
                        intent.putExtra("flag", "1");
                        context.sendBroadcast(intent);
                    }

                    dialog.dismiss();
                } else {
                    baseResponse.getMsg();
                }
            }
        });
    }

    private void initData(Context context, String id) {
        loaddingDialog.show();
        String url = AppConst.BASE_URL + "file/getfolders";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .build();
        Request request = new Request
                .Builder()
                .addHeader("token", SharedPreferencesUtils.getString(context, "token"))
                .url(url)//要访问的链接
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loaddingDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                loaddingDialog.dismiss();
                //Log.d("数据：",response.body().string());
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if (status == 1) {
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            MoveFoldModel moveFoldModel = new MoveFoldModel();
                            JSONObject object1 = array.optJSONObject(i);
                            moveFoldModel.setId(object1.optString("id"));
                            moveFoldModel.setName(object1.optString("name"));
                            moveFoldModel.setIntime(object1.optString("intime"));
                            mMoveData.add(moveFoldModel);
                        }
                        if (!"0".equals(flag)) {
                            MoveFoldModel moveFoldModel = new MoveFoldModel();
                            moveFoldModel.setId("0");
                            moveFoldModel.setIntime("");
                            moveFoldModel.setName("返回根目录");
                            mMoveData.add(0, moveFoldModel);
                        }
                        handler.sendEmptyMessage(0);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 声明Handler对象，并初始化
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // TODO 更新UI
                    Log.d("數據", mMoveData.size() + "");
                    mAdapter.setNewData(mMoveData);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public void showToast(Context context, String msg) {
        ToastUtil toastUtil = new ToastUtil(context, R.layout.toast_center, msg);
        toastUtil.show();
    }


}
