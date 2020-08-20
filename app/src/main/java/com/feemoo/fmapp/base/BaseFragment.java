package com.feemoo.fmapp.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.feemoo.fmapp.MyApplication;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.utils.LoaddingDialog;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.feemoo.fmapp.utils.ToastUtil;

public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    public Context context;
    /**
     * 封装Toast对象
     */
    private static Toast toast;
    private LoaddingDialog loaddingDialog;
    private View rootView;
    public String token;

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        context = ctx;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        token = MyApplication.getToken(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(initLayout(), container, false);
        initView(rootView);
        loaddingDialog = new LoaddingDialog(getActivity());
        initData(context);
        return rootView;
    }

    /**
     * 初始化布局
     *
     * @return 布局id
     */
    protected abstract int initLayout();

    /**
     * 初始化控件
     *
     * @param view 布局View
     */
    protected abstract void initView(final View view);

    /**
     * 初始化、绑定数据
     *
     * @param mContext 上下文
     */
    protected abstract void initData(Context mContext);

    /**
     * 保证同一按钮在1秒内只响应一次点击事件
     */
    public abstract class OnSingleClickListener implements View.OnClickListener {
        //两次点击按钮的最小间隔，目前为1000
        private static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime;

        public abstract void onSingleClick(View view);

        @Override
        public void onClick(View v) {
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                lastClickTime = curClickTime;
                onSingleClick(v);
            }
        }
    }

    /**
     * 同一按钮在短时间内可重复响应点击事件
     */
    public abstract class OnMultiClickListener implements View.OnClickListener {
        public abstract void onMultiClick(View view);

        @Override
        public void onClick(View v) {
            onMultiClick(v);
        }
    }

    public void LoaddingDismiss() {
        if (loaddingDialog != null && loaddingDialog.isShowing()) {
            loaddingDialog.dismiss();
        }
    }

    public void LoaddingShow() {
        if (loaddingDialog == null) {
            loaddingDialog = new LoaddingDialog(getActivity());
        }

        if (!loaddingDialog.isShowing()) {
            loaddingDialog.show();
        }
    }

    public <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    /**
     * 找出对应的控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int id) {

        return (T) getContentView().findViewById(id);
    }

    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return rootView;
    }

    /**
     * 显示提示  toast
     *
     * @param msg 提示信息
     */
//    @SuppressLint("ShowToast")
//    public void showToast(String msg) {
//        try {
//            if (null == toast) {
//                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
//            } else {
//                toast.setText(msg);
//            }
//            toast.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            //解决在子线程中调用Toast的异常情况处理
//            Looper.prepare();
//            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//            Looper.loop();
//        }
//    }
    public void showToast(String msg) {
        ToastUtil toastUtil = new ToastUtil(getActivity(), R.layout.toast_center, msg);
        toastUtil.show();
    }

    protected void toFinish() {
        getActivity().finish();
    }

    public void toActivityFinish(Class activity) {
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
        toFinish();
    }

    public void toActivity(Class activity) {
        Intent intent = new Intent(getActivity(), activity);
        startActivity(intent);
    }

    public void toActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(getActivity(), activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public static String getToken(Context context) {
        String token = SharedPreferencesUtils.getString(context, "token");
        return token;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
