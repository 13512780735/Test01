package com.fmapp.test01.fragment.cloud.star;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.fmapp.test01.R;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.star.FilesModel;
import com.fmapp.test01.network.model.workStation.OnlineFilesModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.LoaddingDialog;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.fmapp.test01.utils.ToastUtil;

import rx.Subscriber;

public class showRenameDialog {
    private LoaddingDialog loaddingDialog;
    private View view;
    private EditText mEtPass;

    public void CenterDialog(Context context, String data,String name,  int position) {
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
        mEtPass.setText(name);
        dialog.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rename(context, data,mEtPass.getText().toString(),position);
                dialog.dismiss();
            }
        });
    }

    /**
     * 重命名
     *
     * @param context
     * @param data
     * @param position
     */
    private void rename(Context context, String  data,String name,int position) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().cltrename(SharedPreferencesUtils.getString(context, "token"),name, Integer.parseInt(data), new Subscriber<BaseResponse<String>>() {
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
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.star");
                    intent.putExtra("id", position);
                    intent.putExtra("flag", "0");
                    context.sendBroadcast(intent);
                }
            }

        });

    }

    public void showToast(Context context, String msg) {
        ToastUtil toastUtil = new ToastUtil(context, R.layout.toast_center, msg);
        toastUtil.show();
    }
}
