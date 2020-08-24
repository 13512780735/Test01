package com.feemoo.fmapp.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * @author txf
 * @create 2020/3/25 0025
 * @
 */
public class UITools {

    /**
     * 设置状态栏 背景颜色
     */
      public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (option != decorView.getSystemUiVisibility()) {
                decorView.setSystemUiVisibility(option);
            }
            if (color != activity.getWindow().getStatusBarColor()) {
                activity.getWindow().setStatusBarColor(color);
            }
        }
    }

    /**
     * 设置状态栏 文字图标颜色 深色或浅色
     *
     * @param dark true 深 false 浅
     */
    public static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decor = activity.getWindow().getDecorView();
            if (dark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }
}
