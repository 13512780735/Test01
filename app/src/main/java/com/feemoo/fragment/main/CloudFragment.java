package com.feemoo.fragment.main;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.feemoo.R;
import com.feemoo.activity.AuthorizationLoginActivity;
import com.feemoo.activity.ScanActivity;
import com.feemoo.activity.cloud.CloudSeachActivity;
import com.feemoo.activity.cloud.DownHisActivity;
import com.feemoo.activity.cloud.DownLoadActivity;
import com.feemoo.activity.cloud.DownLoadListActivity;
import com.feemoo.activity.cloud.DownSeachActivity;
import com.feemoo.activity.cloud.StarSeachActivity;
import com.feemoo.activity.cloud.WorkSeachActivity;
import com.feemoo.base.BaseFragment;
import com.feemoo.fragment.cloud.share.ShareListFragment;
import com.feemoo.fragment.cloud.cloud2.Cloud2Fragment;
import com.feemoo.fragment.cloud.star.StarFragment;
import com.feemoo.fragment.cloud.work.WorkStationFragment;
import com.feemoo.network.Appconst.AppConst;
import com.feemoo.widght.BadgeActionProvider;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloudFragment extends BaseFragment implements View.OnClickListener {
    private RadioGroup mRadioGroup;
    private Toolbar mToolbar;
    private boolean isGetData = false;
    private Cloud2Fragment cloud2Fragment;
    private WorkStationFragment workStationFragment;
    private StarFragment starFragment;
    private ShareListFragment shareListFragment;
    private List<Fragment> list;
    private RadioButton mRb01, mRb02, mRb03, mRb04;
    private String titleName;
    private String flag;
    private View status_bar_view01;
    private BadgeActionProvider mBadgeActionProvider;


    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
//            GetData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    /*   @Override
       public void onResume() {
           super.onResume();
           ImmersionBar.with(this).statusBarDarkFont(true).init();
       }*/
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";

    @Override
    protected void initView(View view) {

//        mTitleName = findView(R.id.tv_title);
//        mHeader01 = findView(R.id.iv_main_header01);
//        mHeader02 = findView(R.id.iv_main_header02);
//        mHeader03 = findView(R.id.iv_main_header03);
//        mTitleName.setText("飞猫云");
//        mHeader01.setImageDrawable(getResources().getDrawable(R.mipmap.icon_search));
//        mHeader02.setImageDrawable(getResources().getDrawable(R.mipmap.icon_add));
//        mHeader03.setImageDrawable(getResources().getDrawable(R.mipmap.icon_down01));
        status_bar_view01 = findView(R.id.status_bar_view01);
        mToolbar = findView(R.id.mToolbar);
        mToolbar.inflateMenu(R.menu.cloud_menu);
        MenuItem menuItem = mToolbar.getMenu().findItem(R.id.action_down);
        mBadgeActionProvider = (BadgeActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mBadgeActionProvider.setBadge(2);
        mBadgeActionProvider.setBadge01(true);
        mBadgeActionProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // ToastUtils.showShort("下载管理");
                toActivity(DownLoadListActivity.class);

            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        if ("1".equals(flag)) {
                            toActivity(CloudSeachActivity.class);
                        } else if ("2".equals(flag)) {
                            toActivity(WorkSeachActivity.class);
                        } else if ("3".equals(flag)) {
                            toActivity(StarSeachActivity.class);
                        } else if ("4".equals(flag)) {
                            toActivity(DownSeachActivity.class);
                        }
                        break;
                    case R.id.action_saoyisao:
//                        showCloudBottomDialog dialog = new showCloudBottomDialog();
//                        dialog.BottomDialog(context);
//                        MorePopWindow morePopWindow = new MorePopWindow(getActivity());
//                        morePopWindow.showPopupWindow(saoyisap);
                        checkCameraPermissions();
                        break;
             /*       case R.id.action_down:

                        break;*/
                    case R.id.action_downhis:
                        toActivity(DownHisActivity.class);
                        break;
                }
                return true;
            }
        });
        mRadioGroup = findView(R.id.mRadioGroup);
        mRb01 = findView(R.id.mRb01);
        mRb02 = findView(R.id.mRb02);
        mRb03 = findView(R.id.mRb03);
        mRb04 = findView(R.id.mRb04);
        cloud2Fragment = new Cloud2Fragment();
        workStationFragment = new WorkStationFragment();
        starFragment = new StarFragment();
        shareListFragment = new ShareListFragment();

        list = new ArrayList<>();
        list.add(cloud2Fragment);
        list.add(workStationFragment);
        list.add(starFragment);
        list.add(shareListFragment);
        mRadioGroup.check(R.id.mRb01);
        titleName = "云空间";
        flag = "1";
        addFragment(cloud2Fragment);


        mRb01.setOnClickListener(this);
        mRb02.setOnClickListener(this);
        mRb03.setOnClickListener(this);
        mRb04.setOnClickListener(this);

    }

    public static final int RC_CAMERA = 0X01;

    /**
     * 检测拍摄权限
     */
    @AfterPermissionGranted(RC_CAMERA)
    private void checkCameraPermissions() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {//有权限
            showPOP();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_camera),
                    RC_CAMERA, perms);
        }
    }

    private void showPOP() {
        // 用于PopupWindow的View
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;//设置阴影透明度
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.cloud_saoyisao_dialog, null, false);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        PopupWindow window = new PopupWindow(contentView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        // 设置PopupWindow的背景

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAtLocation(status_bar_view01, Gravity.BOTTOM, 30, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        //设置出入动画  自定义的
        window.setAnimationStyle(R.style.popwindow_anim_style);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
            }
        });
        TextView tvScan01 = contentView.findViewById(R.id.tv_saoyisao01);
        TextView tvScan02 = contentView.findViewById(R.id.tv_saoyisao02);
        tvScan02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                /*ZxingConfig是配置类
                 *可以设置是否显示底部布局，闪光灯，相册，
                 * 是否播放提示音  震动
                 * 设置扫描框颜色等
                 * 也可以不传这个参数
                 * */
                ZxingConfig config = new ZxingConfig();
                // config.setPlayBeep(false);//是否播放扫描声音 默认为true
                //  config.setShake(false);//是否震动  默认为true
                // config.setDecodeBarCode(false);//是否扫描条形码 默认为true
                config.setReactColor(R.color.button_confirm);//设置扫描框四个角的颜色 默认为白色
                config.setFrameLineColor(R.color.button_confirm);//设置扫描框边框颜色 默认无色
                config.setScanLineColor(R.color.button_confirm);//设置扫描线的颜色 默认白色
                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                config.setShowbottomLayout(false);
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                window.dismiss();
            }
        });
        tvScan01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditScanDialog dialog1 = new showEditScanDialog();
                dialog1.CenterDialog(context);
                window.dismiss();
            }
        });
    }


    //向Activity中添加Fragment的方法
    public void addFragment(Fragment fragment) {

        //获得Fragment管理器
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //使用管理器开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //使用事务替换Fragment容器中Fragment对象
        fragmentTransaction.replace(R.id.main_frag1, fragment);
        //提交事务，否则事务不生效
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRb01:
                addFragment(cloud2Fragment);
                titleName = "云空间";
                flag = "1";
                break;
            case R.id.mRb02:
                addFragment(workStationFragment);
                titleName = "操作台";
                flag = "2";
                break;
            case R.id.mRb03:
                addFragment(starFragment);
                titleName = "星标";
                flag = "3";
                break;
            case R.id.mRb04:
                addFragment(shareListFragment);
                titleName = "我的分享";
                flag = "4";
                break;
        }
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_cloud;
    }


    @Override
    protected void initData(Context mContext) {

    }

    private int REQUEST_CODE_SCAN = 111;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                try {
                    JSONObject object = new JSONObject(content);
                    String ty = object.optString("ty");
                    String cd = object.optString("cd");
                    if ("1".equals(ty)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("cd", content);
                        toActivity(AuthorizationLoginActivity.class, bundle);
                    }
                    if ("5".equals(ty)) {
                        toDown(content);
                    }
                } catch (JSONException e) {
                    showToast("解析失败，不识别当前二维码");
                    e.printStackTrace();
                }
            }
        }
    }

    private void toDown(String content) {
        LoaddingShow();
        String url = AppConst.BASE_URL + "api/scancode";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("code", content)
                .build();
        Request request = new Request
                .Builder()
                .url(url)//要访问的链接
                .post(formBody)
                .addHeader("token", token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoaddingDismiss();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                LoaddingDismiss();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if (status == 1) {
                        String id = object.optJSONObject("data").optString("fid");
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", Integer.valueOf(id));
                        toActivity(DownLoadActivity.class, bundle);

                    } else {
                        Looper.prepare();
                        showToast(msg);
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
