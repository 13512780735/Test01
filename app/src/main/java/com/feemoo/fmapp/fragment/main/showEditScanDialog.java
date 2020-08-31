package com.feemoo.fmapp.fragment.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.cloud.DownLoadActivity;
import com.feemoo.fmapp.network.Appconst.AppConst;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.cloud.FilesModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.LoaddingDialog;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.feemoo.fmapp.utils.ToastUtil;
import com.feemoo.fmapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rx.Subscriber;

public class showEditScanDialog {
    private LoaddingDialog loaddingDialog;
    private View view;
    private EditText mEtPass;

    public void CenterDialog(Context context) {
        loaddingDialog = new LoaddingDialog(context);
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        view = View.inflate(context, R.layout.rename_dialog, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.CENTER);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        window.getDecorView().setPadding(20, 0, 20, 0);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        mEtPass = dialog.findViewById(R.id.edPass);
        dialog.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFastClick()) {
                    String fcode = mEtPass.getText().toString();
                    toDown(context, fcode);
                }
                dialog.dismiss();
            }
        });
    }

    //扫码二维码-下载文件
    private void toDown(Context context, String fcode) {
        loaddingDialog.show();
        String url = AppConst.BASE_URL + "api/scancode";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("fcode", fcode)
                .build();
        Request request = new Request
                .Builder()
                .url(url)//要访问的链接
                .post(formBody)
                .addHeader("token", SharedPreferencesUtils.getString(context, "token"))
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
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if (status == 1) {
                        String id = object.optJSONObject("data").optString("fid");
                        Intent intent = new Intent(context, DownLoadActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", Integer.valueOf(id));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        Looper.prepare();
                        showToast(context, msg);
                        Looper.loop();

                    } else {
                        Looper.prepare();
                        showToast(context, msg);
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void showToast(Context context, String msg) {
        ToastUtil toastUtil = new ToastUtil(context, R.layout.toast_center, msg);
        toastUtil.show();
    }
}
