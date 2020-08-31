package com.feemoo.fmapp.fragment.cloud.share;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.share.FilesModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.LoaddingDialog;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.feemoo.fmapp.utils.ToastUtil;

import rx.Subscriber;


public class showShareBottomDialog {
    private View view;
    private LoaddingDialog loaddingDialog;
    private String shareFlag;
    //type 0 文件类 1，在线压缩包

    public void BottomDialog(Context context, FilesModel data, String type, int position) {
        loaddingDialog = new LoaddingDialog(context);
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        view = View.inflate(context, R.layout.share_dialog, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        shareFlag = SharedPreferencesUtils.getString(context, "share");
        dialog.findViewById(R.id.tv_share01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(data.getLink());
                showToast(context, "本文件分享地址已复制剪切板，请前往粘贴使用");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShare(context, data, position);
                dialog.dismiss();
            }
        });


    }

    /**
     * 云空间公有文件/私有文件转换
     *
     * @param context
     * @param data
     * @param position
     */
    private void setShare(Context context, FilesModel data, int position) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().setflshare(SharedPreferencesUtils.getString(context, "token"), data.getId(), "0", new Subscriber<BaseResponse<String>>() {
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
            public void onNext(BaseResponse<String> stringBaseResponse) {
                loaddingDialog.dismiss();
                if ("1".equals(stringBaseResponse.getStatus())) {
                    showToast(context, stringBaseResponse.getMsg());

                    if ("1".equals(shareFlag)) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.shareSearch");
                        intent.putExtra("id", position);
                        intent.putExtra("flag", "");
                        context.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.share");
                        intent.putExtra("id", position);
                        intent.putExtra("flag", "");
                        context.sendBroadcast(intent);
                    }
                } else {
                    showToast(context, stringBaseResponse.getMsg());
                }
            }
        });
    }

    public void showToast(Context context, String msg) {
        ToastUtil toastUtil = new ToastUtil(context, R.layout.toast_center, msg);
        toastUtil.show();
    }


}
