package com.feemoo.fragment.cloud.star;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.feemoo.R;
import com.feemoo.network.model.BaseResponse;
import com.feemoo.network.util.DataResultException;
import com.feemoo.network.util.RetrofitUtil;
import com.feemoo.utils.LoaddingDialog;
import com.feemoo.utils.SharedPreferencesUtils;
import com.feemoo.utils.ToastUtil;
import com.feemoo.utils.Utils;

import rx.Subscriber;

public class showRenameDialog {
    private LoaddingDialog loaddingDialog;
    private View view;
    private EditText mEtPass;

    public void CenterDialog(Context context, String data, String name, int position) {
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
                if (Utils.isFastClick()) {
                    rename(context, data, mEtPass.getText().toString(), position);
                }
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
    private void rename(Context context, String data, String name, int position) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().cltrename(SharedPreferencesUtils.getString(context, "token"), name, Integer.parseInt(data), new Subscriber<BaseResponse<String>>() {
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
