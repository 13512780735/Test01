package com.fmapp.test01.fragment.cloud;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.fmapp.test01.R;
import com.fmapp.test01.activity.cloud.DownLoadListActivity;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.SvipDownModel;
import com.fmapp.test01.network.model.workStation.FilesModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.CustomDialog;
import com.fmapp.test01.utils.LoaddingDialog;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.fmapp.test01.utils.ToastUtil;

import java.io.File;
import java.sql.SQLException;

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
                toDownLoad(context, data);
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
     * 下载
     *
     * @param context
     */
    private void toDownLoad(Context context, FilesModel data) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().svipdown(SharedPreferencesUtils.getString(context, "token"), Integer.parseInt(data.getId()), new Subscriber<BaseResponse<SvipDownModel>>() {
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
            public void onNext(BaseResponse<SvipDownModel> baseResponse) {
                loaddingDialog.dismiss();
                if ("1".equals(baseResponse.getStatus()) ) {
                    String link = baseResponse.getData().getLink();
                    String ext = baseResponse.getData().getExtension();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("link", link);
//                    bundle.putString("ext", ext);
//                    bundle.putString("id", downLoadModel.getId());
//                    bundle.putString("name", downLoadModel.getName());
//                    bundle.putString("size", downLoadModel.getSize());
                    /*将任务添加到下载队列，下载器会自动开始下载*/
                    addTask(context, link, data);
                    Intent intent = new Intent(context, DownLoadListActivity.class);
                    context.startActivity(intent);

                }
            }

        });
    }

    private void addTask(Context context, String link, FilesModel data) {
//        downloadManager = DownloadService.getDownloadManager(context.getApplicationContext());
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "file");
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        String path = file.getAbsolutePath().concat("/").concat(data.getName());
//        downloadInfo = new DownloadInfo.Builder().setUrl(link)
//                .setPath(path)
//                .build();
//        downloadManager.download(downloadInfo);
//        downloadManager.resume(downloadInfo);
//        String icon = "https://android-artworks.25pp.com/fs08/2018/11/28/9/110_5eb0ac0aebf3abcf91590b8a0a320630_con_130x130.png";
//        MyBusinessInfLocal myBusinessInfLocal = new MyBusinessInfLocal(
//                data.getId(), data.getName(), icon, link);
//        try {
//            DBController.getInstance(context.getApplicationContext()).createOrUpdateMyDownloadInfo(myBusinessInfLocal);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

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
                if ("1".equals(baseResponse.getStatus()) ) {
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
                if ("1".equals(baseResponse.getStatus()) ) {
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
