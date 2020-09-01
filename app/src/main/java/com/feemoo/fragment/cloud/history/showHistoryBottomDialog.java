package com.feemoo.fragment.cloud.history;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.feemoo.R;
import com.feemoo.activity.cloud.DownLoadActivity;
import com.feemoo.network.model.BaseResponse;
import com.feemoo.network.model.history.FilesModel;
import com.feemoo.network.util.DataResultException;
import com.feemoo.network.util.RetrofitUtil;
import com.feemoo.utils.CustomDialog;
import com.feemoo.utils.LoaddingDialog;
import com.feemoo.utils.SharedPreferencesUtils;
import com.feemoo.utils.ToastUtil;

import rx.Subscriber;

public class showHistoryBottomDialog {
    private View view;
    private LoaddingDialog loaddingDialog;
    //type 0 文件类 1，在线压缩包

    public void BottomDialog(Context context, FilesModel data, String type, int position) {
        loaddingDialog = new LoaddingDialog(context);
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        view = View.inflate(context, R.layout.history_dialog, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

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
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
//                            Intent intent = new Intent();
//                            intent.setAction("android.intent.action.cloud");
//                            intent.putExtra("id", position);
//                            context.sendBroadcast(intent);
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
                            .setNegativeButton("取消",  new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setPositiveButton("确定",  new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    del(context, data.getFid(), position);
//                                Intent intent = new Intent();
//                                intent.setAction("android.intent.action.cloud");
//                                intent.putExtra("id", position);
//                                context.sendBroadcast(intent);
                                }
                            });
                    dialog1.show();
                dialog.dismiss();
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
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.history");
                    intent.putExtra("id", position);
                    context.sendBroadcast(intent);
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
