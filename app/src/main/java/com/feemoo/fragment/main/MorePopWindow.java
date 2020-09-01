package com.feemoo.fragment.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.feemoo.R;

public class MorePopWindow extends PopupWindow {
    private OnPopWindowItemClickListener listener;

    public interface OnPopWindowItemClickListener {

        void onScanEditClick();

        void onScanClick();
    }

    @SuppressLint("InflateParams")
    public MorePopWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.cloud_saoyisao_dialog, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimationPreview);


        TextView re_scanner = (TextView) content.findViewById(R.id.tv_saoyisao01);
        TextView re_sao = (TextView) content.findViewById(R.id.tv_saoyisao02);
        re_scanner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onScanEditClick();
                }
                dismiss();
            }
        });
        re_sao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onScanClick();
                }
                dismiss();

            }
        });


    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
