package com.feemoo.activity.MyInfo;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.feemoo.R;
import com.feemoo.activity.cloud.DownLoadActivity;
import com.feemoo.fragment.cloud.cloud2.showMoveBottomDialog;
import com.feemoo.fragment.cloud.cloud2.showRenameDialog;
import com.feemoo.network.model.BaseResponse;
import com.feemoo.network.model.cloud.FilesModel;
import com.feemoo.network.util.DataResultException;
import com.feemoo.network.util.RetrofitUtil;
import com.feemoo.utils.CustomDialog;
import com.feemoo.utils.LoaddingDialog;
import com.feemoo.utils.SharedPreferencesUtils;
import com.feemoo.utils.ToastUtil;
import com.feemoo.widght.BorderTextView;

import rx.Subscriber;

public class showSignCenterDialog {
    private View view;
    private LoaddingDialog loaddingDialog;
    //type 0 文件类 1，在线压缩包 2 搜索

    public void CenterDialog(Context context, String num) {
        loaddingDialog = new LoaddingDialog(context);
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        view = View.inflate(context, R.layout.sign_dialog, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.CENTER);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
       //ws window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
//getMetricsWidth(context) 得到屏幕宽度
        params.width = getMetricsWidth(context) / 2;
//设置dialog宽度，高度相同处理即可   需在dialog.show()或者setContentView(view)后设置方有效果

        dialog.getWindow().setAttributes(params);

        dialog.show();
        ImageView ivDel = dialog.findViewById(R.id.ivDel);
        TextView tvContent = dialog.findViewById(R.id.tvContent);
        BorderTextView tvConfirm = dialog.findViewById(R.id.tvConfirm);
        ivDel.setOnClickListener(view -> dialog.dismiss());
        tvConfirm.setOnClickListener(view -> dialog.dismiss());
        tvContent.setText(num);
    }

    /**
     * 获取手机屏幕尺寸 宽度
     *
     * @param context 上下文
     * @return int
     */
    private int getMetricsWidth(Context context) {
        // String str = "";
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;// 屏幕高（像素，如：800px）
        return screenWidth;
    }

    public void showToast(Context context, String msg) {
        ToastUtil toastUtil = new ToastUtil(context, R.layout.toast_center, msg);
        toastUtil.show();
    }


}
