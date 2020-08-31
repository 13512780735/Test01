package com.feemoo.fmapp.activity.file;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.file.zip.ZipRarFileActivity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.workStation.OnlineFilesModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.LoaddingDialog;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.feemoo.fmapp.utils.ToastUtil;

import rx.Subscriber;

public class showOnlineDialog {
    private LoaddingDialog loaddingDialog;
    private View view;
    private EditText mEtPass;

    public void CenterDialog(Context context,  String data, String path,String name, int position) {
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
               // toOnline(context, data);


                Intent intent = new Intent(context, ZipRarFileActivity.class);
                intent.putExtra("zipRarFilePath", path);
                intent.putExtra("zipRarFileName", name);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    /**
     * 在线解压
     *
     * @param context
     * @param data
     */
    private void toOnline(Context context, String data) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().getzip(SharedPreferencesUtils.getString(context, "token"), Integer.parseInt(data), 0, mEtPass.getText().toString(), new Subscriber<BaseResponse<OnlineFilesModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loaddingDialog.dismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    showToast( context,resultException.getMsg());
                }
            }

            @Override
            public void onNext(BaseResponse<OnlineFilesModel> baseResponse) {
                loaddingDialog.dismiss();
                if ("1".equals(baseResponse.getStatus()) ) {
                } else {
                    showToast( context,baseResponse.getMsg());
                }
            }

        });
    }

    public void showToast(Context context, String msg) {
        ToastUtil toastUtil = new ToastUtil(context, R.layout.toast_center, msg);
        toastUtil.show();
    }
}
