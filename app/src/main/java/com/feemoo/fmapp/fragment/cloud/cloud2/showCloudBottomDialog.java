package com.feemoo.fmapp.fragment.cloud.cloud2;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.cloud.DownLoadActivity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.FSLModel;
import com.feemoo.fmapp.network.model.cloud.FilesModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.CustomDialog;
import com.feemoo.fmapp.utils.LoaddingDialog;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.feemoo.fmapp.utils.ToastUtil;


import rx.Subscriber;

public class showCloudBottomDialog {
    private View view;
    private LoaddingDialog loaddingDialog;
    private String cloudFlag;
    //type 0 文件类 1，在线压缩包

    public void BottomDialog(Context context, FilesModel data, String type, int position) {
        loaddingDialog = new LoaddingDialog(context);
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        view = View.inflate(context, R.layout.cloud_dialog, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        cloudFlag = SharedPreferencesUtils.getString(context, "cloud");
        Log.d("cloudFlag", cloudFlag + "aaa");
        dialog.findViewById(R.id.tv_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DownLoadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", Integer.parseInt(data.getId()));
                intent.putExtras(bundle);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toShare(context, data, position);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoveBottomDialog dialog1 = new showMoveBottomDialog();
                dialog1.BottomDialog(context, data, position);

                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_cloud).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddingDialog.show();
                RetrofitUtil.getInstance().tospro(SharedPreferencesUtils.getString(context, "token"), Integer.parseInt(data.getId()), new Subscriber<BaseResponse<String>>() {
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
                        } else {
                            showToast(context, baseResponse.getMsg());
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_rename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showRenameDialog dialog1 = new showRenameDialog();
                    dialog1.CenterDialog(context, data, data.getBasename(), position);
                dialog.dismiss();
            }

        });
        dialog.findViewById(R.id.tv_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CustomDialog dialog1 = new CustomDialog(context).builder()
                            .setGravity(Gravity.CENTER)
                            .setTitle("提示", context.getResources().getColor(R.color.black))//可以不设置标题颜色，默认系统颜色
                            .setSubTitle("是否删除该文件")
                            .setNegativeButton("取消", R.color.button_confirm, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setPositiveButton("确定", R.color.button_confirm, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    del(context, data.getId(), position);
                                }
                            });
                    dialog1.show();
                dialog.dismiss();
            }
        });


    }

    /**
     * 分享
     *
     * @param context
     * @param data
     * @param position
     */
    private void toShare(Context context, FilesModel data, int position) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().getfsl(SharedPreferencesUtils.getString(context, "token"), Integer.parseInt(data.getId()), new Subscriber<BaseResponse<FSLModel>>() {
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
            public void onNext(BaseResponse<FSLModel> baseResponse) {
                loaddingDialog.dismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(baseResponse.getData().getLink());
                    showToast(context, "本文件分享地址已复制剪切板，请前往粘贴使用");
                } else {
                    showToast(context, baseResponse.getMsg());
                }
            }
        });


    }

    /**
     * 删除文件
     *
     * @param context
     * @param id
     * @param position
     */
    private void del(Context context, String id, int position) {

        loaddingDialog.show();

        RetrofitUtil.getInstance().delfile(SharedPreferencesUtils.getString(context, "token"), Integer.valueOf(id), new Subscriber<BaseResponse<String>>() {
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
                        intent.putExtra("id", position);
                        intent.putExtra("flag", "1");
                        context.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.cloud");
                        intent.putExtra("id", position);
                        intent.putExtra("flag", "1");
                        context.sendBroadcast(intent);
                    }
                }
                showToast(context, baseResponse.getMsg());
            }
        });
    }

    public void showToast(Context context, String msg) {
        ToastUtil toastUtil = new ToastUtil(context, R.layout.toast_center, msg);
        toastUtil.show();
    }


}
