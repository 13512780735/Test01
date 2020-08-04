package com.fmapp.test01.fragment.cloud;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.fmapp.test01.R;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.workStation.FilesModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.CustomDialog;
import com.fmapp.test01.utils.LoaddingDialog;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.fmapp.test01.utils.ToastUtil;

import rx.Subscriber;

public class showworkBottomDialog {
    private View view;
    private LoaddingDialog loaddingDialog;
    //type 0 文件类 1，在线压缩包

    public void BottomDialog(Context context, FilesModel data, String type, int position) {
        loaddingDialog = new LoaddingDialog(context);
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        view = View.inflate(context, R.layout.work_dialog, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv_online = view.findViewById(R.id.tv_online);
        TextView tv_look01 = view.findViewById(R.id.tv_look01);
        TextView tv_look = view.findViewById(R.id.tv_look);
        if ("zip".equals(data.getExt())) {
            tv_online.setVisibility(View.VISIBLE);
            tv_look.setVisibility(View.VISIBLE);
        }
        if ("png".equals(data.getExt())) {
            tv_look01.setVisibility(View.VISIBLE);
        }
        dialog.show();

        dialog.findViewById(R.id.tv_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, DownLoadActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("id", Integer.parseInt(data.getId()));
//                intent.putExtras(bundle);
//                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_online).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOnlineDialog dialog01 = new showOnlineDialog();
                dialog01.CenterDialog(context, data.getId(), "0", position);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_look).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                toEverCloud(context, data.getId(), position);
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
     * 推至永存空间
     *
     * @param context
     * @param id
     * @param position
     */
    private void toEverCloud(Context context, String id, int position) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().toever(SharedPreferencesUtils.getString(context, "token"), Integer.parseInt(id), new Subscriber<BaseResponse<String>>() {
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
                if (baseResponse.getStatus() == 1) {
                    showToast(context, baseResponse.getMsg());
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.work");
                    intent.putExtra("id", position);
                    intent.putExtra("flag", "0");
                    context.sendBroadcast(intent);
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

        RetrofitUtil.getInstance().delprofile(SharedPreferencesUtils.getString(context, "token"), Integer.valueOf(id), new Subscriber<BaseResponse<String>>() {
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
                if (baseResponse.getStatus() == 1) {
                    showToast(context, baseResponse.getMsg());
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.work");
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
