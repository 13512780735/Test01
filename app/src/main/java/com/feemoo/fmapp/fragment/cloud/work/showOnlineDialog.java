package com.feemoo.fmapp.fragment.cloud.work;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.file.ZipPreviewActivity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.workStation.OnlineFilesModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.LoaddingDialog;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.feemoo.fmapp.utils.ToastUtil;
import com.feemoo.fmapp.utils.Utils;

import rx.Subscriber;

public class showOnlineDialog {
    private LoaddingDialog loaddingDialog;
    private View view;
    private EditText mEtPass;

    /**
     * type为0是在线解压
     * 为1是在线预览
     *
     * @param context
     * @param data
     * @param type
     * @param position
     */
    public void CenterDialog(Context context, String data, String name, String type, int position) {
        loaddingDialog = new LoaddingDialog(context);
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        view = View.inflate(context, R.layout.online_dialog, null);
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
                    if ("0".equals(type)) {
                        toOnline(context, data, type);
                    } else {
                        toOnline1(context, data, type, name);
                    }
                }
                dialog.dismiss();
            }
        });
    }

    private void toOnline1(Context context, String data, String type, String name) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().getzip(SharedPreferencesUtils.getString(context, "token"), Integer.parseInt(data), 1, mEtPass.getText().toString(), new Subscriber<BaseResponse<OnlineFilesModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loaddingDialog.dismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    Looper.prepare();
                    showToast( context,resultException.getMsg());
                    Looper.loop();
                }
            }

            @Override
            public void onNext(BaseResponse<OnlineFilesModel> baseResponse) {
                loaddingDialog.dismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    Intent intent = new Intent(context, ZipPreviewActivity.class);
                    intent.putExtra("url", baseResponse.getData().getUrl());
                    intent.putExtra("name", name);
                    context.startActivity(intent);
                } else {
                    Looper.prepare();
                    showToast( context,baseResponse.getMsg());
                    Looper.loop();
                }
            }

        });
    }

    /**
     * 在线解压
     *
     * @param context
     * @param type
     * @param data
     */
    private void toOnline(Context context, String data, String type) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().zip(SharedPreferencesUtils.getString(context, "token"), Integer.parseInt(data), 0, mEtPass.getText().toString(), new Subscriber<BaseResponse<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loaddingDialog.dismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    Looper.prepare();
                    showToast( context,resultException.getMsg());
                    Looper.loop();
                }
            }

            @Override
            public void onNext(BaseResponse<String> baseResponse) {
                loaddingDialog.dismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    Looper.prepare();
                    showToast(context, baseResponse.getMsg());
                    Looper.loop();
                } else {
                    Looper.prepare();
                    showToast(context, baseResponse.getMsg());
                    Looper.loop();
                }
            }

        });
    }

    public void showToast(Context context, String msg) {
        ToastUtil toastUtil = new ToastUtil(context, R.layout.toast_center, msg);
        toastUtil.show();
    }
}
