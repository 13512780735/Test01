package com.feemoo.fmapp.fragment.cloud.work;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.androidev.download.DownloadInfo;
import com.androidev.download.DownloadManager;
import com.androidev.download.DownloadTask;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.cloud.DownLoadListActivity;
import com.feemoo.fmapp.activity.file.PictureActivity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.SvipDownModel;
import com.feemoo.fmapp.network.model.workStation.FilesModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.CustomDialog;
import com.feemoo.fmapp.utils.LoaddingDialog;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.feemoo.fmapp.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class showworkBottomDialog {
    private View view;
    private LoaddingDialog loaddingDialog;
    private List<DownloadTask> tasks;
    private String key;
    private List<DownloadInfo> mFinishData;
    private String workFlag;
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
        workFlag = SharedPreferencesUtils.getString(context, "work");//1是永存空间过来
        Log.d("workFlag", workFlag);
        TextView tv_online = view.findViewById(R.id.tv_online);
        TextView tv_look01 = view.findViewById(R.id.tv_look01);
        TextView tv_look = view.findViewById(R.id.tv_look);
        TextView tv_cloud = view.findViewById(R.id.tv_cloud);
        if ("zip".equals(data.getExt())) {
            tv_online.setVisibility(View.VISIBLE);
            tv_look.setVisibility(View.VISIBLE);
        }
        if ("png".equals(data.getExt())) {
            tv_look01.setVisibility(View.VISIBLE);
        }

        if ("1".equals(workFlag)) {
            tv_cloud.setVisibility(View.GONE);
        } else tv_cloud.setVisibility(View.VISIBLE);
        dialog.show();

        dialog.findViewById(R.id.tv_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    toDownLoad(context, data);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_online).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showOnlineDialog dialog01 = new showOnlineDialog();
                    dialog01.CenterDialog(context, data.getId(), data.getName(), "0", position);
                dialog.dismiss();
            }
        });
        //图片在线预览
        dialog.findViewById(R.id.tv_look01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    getLookMac(context, data, position);
                dialog.dismiss();
            }
        });
        //压缩包在线预览
        dialog.findViewById(R.id.tv_look).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showOnlineDialog dialog01 = new showOnlineDialog();
                dialog01.CenterDialog(context, data.getId(), data.getName(), "1", position);
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
     * 在线预览图片
     *
     * @param context
     * @param data
     * @param position
     */
    private void getLookMac(Context context, FilesModel data, int position) {
        loaddingDialog.show();
        RetrofitUtil.getInstance().prodown(SharedPreferencesUtils.getString(context, "token"), Integer.parseInt(data.getId()), 1, new Subscriber<BaseResponse<SvipDownModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loaddingDialog.dismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    Looper.prepare();
                    showToast(context, resultException.getMsg());
                    Looper.loop();
                }
            }

            @Override
            public void onNext(BaseResponse<SvipDownModel> baseResponse) {
                loaddingDialog.dismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    String link = baseResponse.getData().getLink();
                    String ext = baseResponse.getData().getExtension();
                    Intent intent = new Intent(context, PictureActivity.class);
                    intent.putExtra("uri", link);
                    intent.putExtra("name", data.getName());
                    intent.putExtra("flag", "1");
                    context.startActivity(intent);

                } else {
                    Looper.prepare();
                    showToast(context, baseResponse.getMsg());
                    Looper.loop();
                }

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
        RetrofitUtil.getInstance().prodown(SharedPreferencesUtils.getString(context, "token"), Integer.parseInt(data.getId()), 0, new Subscriber<BaseResponse<SvipDownModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                loaddingDialog.dismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    Looper.prepare();
                    showToast(context, resultException.getMsg());
                    Looper.loop();
                }
            }

            @Override
            public void onNext(BaseResponse<SvipDownModel> baseResponse) {
                loaddingDialog.dismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    String link = baseResponse.getData().getLink();
                    String ext = baseResponse.getData().getExtension();
                    addTask(context, link, data);
                    Intent intent = new Intent(context, DownLoadListActivity.class);
                    context.startActivity(intent);

                } else {
                    Looper.prepare();
                    showToast(context, baseResponse.getMsg());
                    Looper.loop();
                }
            }

        });
    }

    private void addTask(Context context, String link, FilesModel data) {
        DownloadManager controller = DownloadManager.getInstance();
        tasks = new ArrayList<>();
        tasks.add(controller.newTask(Integer.parseInt(data.getId()), link, data.getName()).extras(data.getSize()).create());
        key = tasks.get(0).key;
        mFinishData = controller.getAllInfo();
        boolean flag = false;
        for (int i = 0; i < mFinishData.size(); i++) {
            if (mFinishData.get(i).key.contains(key)) {
                flag = true;
                break;
            } else {
                flag = false;
            }
        }

        if (flag) {
            showToast(context, "该文件已经在下载列表中");
        } else {
            tasks.get(0).start();
            Intent intent = new Intent(context, DownLoadListActivity.class);
            context.startActivity(intent);
        }
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
                    Looper.prepare();
                    showToast(context, resultException.getMsg());
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
                    if ("1".equals(workFlag)) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.liveSpace");
                        intent.putExtra("id", position);
                        intent.putExtra("flag", "0");
                        context.sendBroadcast(intent);
                    } else if ("2".equals(workFlag)) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.workFile");
                        intent.putExtra("id", position);
                        intent.putExtra("flag", "0");
                        context.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.work");
                        intent.putExtra("id", position);
                        intent.putExtra("flag", "0");
                        context.sendBroadcast(intent);
                    }
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
                    Looper.prepare();
                    showToast(context, resultException.getMsg());
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
                    if ("1".equals(workFlag)) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.liveSpace");
                        intent.putExtra("id", position);
                        context.sendBroadcast(intent);
                    } else if ("2".equals(workFlag)) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.workFile");
                        intent.putExtra("id", position);
                        context.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.work");
                        intent.putExtra("id", position);
                        context.sendBroadcast(intent);
                    }
                }
                Looper.prepare();
                showToast(context, baseResponse.getMsg());
                Looper.loop();
            }
        });
    }

    public void showToast(Context context, String msg) {
        ToastUtil toastUtil = new ToastUtil(context, R.layout.toast_center, msg);
        toastUtil.show();
    }


}
