package com.feemoo.fmapp.fragment.main;


import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feemoo.fmapp.MyApplication;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.AuthorizationLoginActivity;
import com.feemoo.fmapp.activity.cloud.DownLoadActivity;
import com.feemoo.fmapp.activity.cloud.DownLoadListActivity;
import com.feemoo.fmapp.activity.select.JXSeachActivity;
import com.feemoo.fmapp.activity.select.ProjectDetailsActivity;
import com.feemoo.fmapp.adapter.SelectListAdapter;
import com.feemoo.fmapp.base.BaseFragment;
import com.feemoo.fmapp.fragment.select.showSelectBottomDialog;
import com.feemoo.fmapp.network.Appconst.AppConst;
import com.feemoo.fmapp.network.model.JXNavModel;
import com.feemoo.fmapp.network.model.select.JXHomeModel;
import com.feemoo.fmapp.utils.DensityUtil;
import com.feemoo.fmapp.utils.SharedPreferencesUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFragment extends BaseFragment implements SwipeRefreshAdapterView.OnListLoadListener, SwipeRefreshLayout.OnRefreshListener {

    /*   private TextView mTitleName;
       private ImageView mHeader01;
       private ImageView mHeader02;
       private ImageView mHeader03;*/
    private boolean isGetData = false;
    private List<JXNavModel> JxNavData;
    private List<String> titles = new ArrayList<>();
    private RadioGroup mRadioGroup;
    private SwipeRefreshRecyclerView mRecycleView;
    List<JXHomeModel> jxHomeModelList = new ArrayList<>();
    private SelectListAdapter mAdapter;
    private String atype, scid, scid2, keywords;
    private int flag;
    private int tag = 0;
    private MBroadcastReceiver receiver;
    private Toolbar mToolbar;

    //public static SelectFragment newInstance() {
    //        return new SelectFragment();
    //    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetData();
    }

//    @Override
//    public void initImmersionBar() {
//        super.initImmersionBar();
//        ImmersionBar.with(this).statusBarDarkFont(true);
//    }


    @Override
    protected void initView(View v) {

        register();
        mToolbar = findView(R.id.mToolbar);
        mToolbar.inflateMenu(R.menu.home_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        toActivity(JXSeachActivity.class);
                        break;
                    case R.id.action_saoyisao:
                        checkCameraPermissions();
                        break;
                    case R.id.action_down:
                        toActivity(DownLoadListActivity.class);
                        break;
                }
                return true;
            }
        });
        //        mTitleName = findView(R.id.tv_title);
        //        mHeader01 = findView(R.id.iv_main_header01);
        //        mHeader02 = findView(R.id.iv_main_header02);
        //        mHeader03 = findView(R.id.iv_main_header03);
        //        mTitleName.setText("鲸选下载");
        //        mHeader01.setImageDrawable(getResources().getDrawable(R.mipmap.icon_search));
        //        mHeader02.setImageDrawable(getResources().getDrawable(R.mipmap.icon_saoyisao));
        //        mHeader03.setImageDrawable(getResources().getDrawable(R.mipmap.icon_down01));
        //        mHeader01.setOnClickListener(view -> showToast("点击了"));
        mRadioGroup = findView(R.id.mRadioGroup);
        mRecycleView = findView(R.id.recycler_view);
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(),
                2));
        mRecycleView.setOnListLoadListener(this);

        mRecycleView.setOnRefreshListener(this);
        mAdapter = new SelectListAdapter(R.layout.select_list_item, jxHomeModelList);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        atype = String.valueOf(0);
        scid = String.valueOf(0);
        scid2 = String.valueOf(0);
        flag = 0;
        keywords = "";

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                RadioButton tempButton = findViewById(checkId);
                tempButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("checkId", checkId + "");
                        flag = checkId;

                        if (checkId == 0) {
                            tag = checkId;
                            if (jxHomeModelList.size() > 0) {
                                jxHomeModelList.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                            GetHomeData();
                            atype = String.valueOf(0);
                            scid = String.valueOf(0);
                            scid2 = String.valueOf(0);
                            keywords = "";
                        } else {
                            if (tag == checkId) {
                                tag = checkId;
                                showSelectBottomDialog dialog = new showSelectBottomDialog();
                                dialog.BottomDialog(getContext(), JxNavData, checkId - 1);
                            } else {
                                tag = checkId;
                                if (jxHomeModelList.size() > 0) {
                                    jxHomeModelList.clear();
                                    mAdapter.notifyDataSetChanged();
                                }
                                pg = 1;
                                atype = JxNavData.get(checkId - 1).getId();
                                scid = String.valueOf(0);
                                scid2 = String.valueOf(0);
                                keywords = "";
                                GetHomeData2();
                            }
                        }
                    }
                });
            }
        });

    }

    public static final int RC_CAMERA = 0X01;

    /**
     * 检测拍摄权限
     */
    @AfterPermissionGranted(RC_CAMERA)
    private void checkCameraPermissions() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {//有权限
            toScan();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_camera),
                    RC_CAMERA, perms);
        }
    }

    private int REQUEST_CODE_SCAN = 111;

    private void toScan() {
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
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_select;
    }


    private void GetData() {
        String url = AppConst.BASE_URL + "choice/getjxnav";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request
                .Builder()
                .addHeader("token", SharedPreferencesUtils.getString(getContext(), "token"))
                .url(url)//要访问的链接
                .post(formBody)
                .build();
        Call call = client.newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if ("1".equals(String.valueOf(status))) {
                        JSONArray array = object.optJSONArray("data");
                        JxNavData = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.optJSONObject(i);
                            JXNavModel jxNavModel = new JXNavModel();
                            jxNavModel.setId(object1.optString("id"));
                            jxNavModel.setPid(object1.optString("pid"));
                            jxNavModel.setName(object1.optString("name"));
                            JSONArray childData = object1.optJSONArray("child");
                            if (childData.length() > 0) {
                                List<JXNavModel.ChildBeanX> childBeanXList = new ArrayList<>();
                                for (int j = 0; j < childData.length(); j++) {
                                    JSONObject object2 = childData.optJSONObject(j);
                                    JXNavModel.ChildBeanX childBeanX = new JXNavModel.ChildBeanX();
                                    childBeanX.setId(object2.optString("id"));
                                    childBeanX.setPid(object2.optString("pid"));
                                    childBeanX.setName(object2.optString("name"));
                                    JSONArray childData1 = object2.optJSONArray("child");
                                    if (childData1.length() > 0) {
                                        List<JXNavModel.ChildBeanX.ChildBean> childBeanList = new ArrayList<>();
                                        for (int z = 0; z < childData1.length(); z++) {
                                            JSONObject object3 = childData1.optJSONObject(z);
                                            JXNavModel.ChildBeanX.ChildBean childBean = new JXNavModel.ChildBeanX.ChildBean();
                                            childBean.setId(object3.optString("id"));
                                            childBean.setPid(object3.optString("pid"));
                                            childBean.setName(object3.optString("name"));
                                            JSONArray childData2 = object3.optJSONArray("child");
                                            if (childData2.length() > 0) {
                                                childBean.setChild(null);
                                            }
                                            childBeanList.add(childBean);
                                        }
                                        childBeanX.setChild(childBeanList);
                                    }
                                    childBeanXList.add(childBeanX);
                                }
                                jxNavModel.setChild(childBeanXList);
                            }
                            JxNavData.add(jxNavModel);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //更改UI；
                                initTab();
                            }
                        });
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


    //获取tab名称
    private void initTab() {
        for (int i = 0; i < JxNavData.size(); i++) {
            titles.add(JxNavData.get(i).getName());
        }
        titles.add(0, "推荐");
        RadioButton mRadioButton;
        //必须用RadioGroup的LayoutParams  ，而不是LinearLayout的
        RadioGroup.LayoutParams mButtonLayoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < titles.size(); i++) {
            mRadioButton = new RadioButton(getActivity());
            mRadioButton.setId(i); //必须要设置一个ID
            mRadioButton.setTag(i);
            mButtonLayoutParams.setMargins(10, 0, 10, 0);
            mRadioButton.setText(titles.get(i));
            mRadioButton.setBackground(getActivity().getResources().getDrawable(R.drawable.rb_main_background));
            mRadioButton.setTextColor(getActivity().getResources().getColorStateList(R.color.rb_select_textcolor));
            mRadioButton.setWidth(DensityUtil.dip2px(getActivity(), 100));
            mRadioButton.setGravity(Gravity.CENTER);
            Bitmap a = null;
            mRadioButton.setButtonDrawable(new BitmapDrawable(a));
            mRadioButton.setPadding(10, DensityUtil.dip2px(getActivity(), 10), 10, DensityUtil.dip2px(getActivity(), 10));
            if (i == 0) {
                mRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else {
                mRadioButton.setCompoundDrawablesWithIntrinsicBounds(null, null, makeDrawable(R.drawable.rg_down_bg), null);
            }
            mRadioGroup.addView(mRadioButton, mButtonLayoutParams);
        }
        mRadioGroup.check(0);
        GetHomeData();
    }

    /**
     * 鲸选首页-进入首页后展示的鲸选数据
     */
    private void GetHomeData() {
        LoaddingShow();
        String url = AppConst.BASE_URL + "choice/getjxtopfile";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request
                .Builder()
                .addHeader("token", SharedPreferencesUtils.getString(getContext(), "token"))
                .url(url)//要访问的链接
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoaddingDismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LoaddingDismiss();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if ("1".equals(String.valueOf(status))) {
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.optJSONObject(i);
                            JXHomeModel jxHomeModel = new JXHomeModel();
                            jxHomeModel.setId(object1.optString("id"));
                            jxHomeModel.setImg(object1.optString("img"));
                            jxHomeModel.setName(object1.optString("name"));
                            jxHomeModel.setTag(object1.optString("tag"));
                            jxHomeModelList.add(jxHomeModel);

                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setNewData(jxHomeModelList);
                                mAdapter.notifyDataSetChanged();
                            }
                        });

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

    private Drawable makeDrawable(int id) {
        Resources resources = MyApplication.getInstance().getResources();
        Drawable drawable = resources.getDrawable(id);
        //前两个参数为X轴和Y轴的起点,后两个参数是宽高
        drawable.setBounds(1, 1, drawable.getMinimumHeight(), drawable.getMinimumWidth());
        return drawable;
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    @Override
    protected void initData(Context mContext) {


    }

    int pg;

    @Override
    public void onRefresh() {
        pg = 1;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (jxHomeModelList.size() > 0) {
                    jxHomeModelList.clear();
                    mAdapter.notifyDataSetChanged();
                }

                if (flag == 0) {
                    GetHomeData();
                } else {
                    GetHomeData2();
                }
                mAdapter.notifyDataSetChanged();
                mRecycleView.setRefreshing(false);
            }
        }, 1000);


    }


    @Override
    public void onListLoad() {
        ++pg;
        mRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mRecycleView.setLoading(false);
                if (pg == 1) {
                    mRecycleView.setLoadCompleted(true);
                } else GetHomeData2();
                mRecycleView.setLoadCompleted(true);
            }
        }, 2000);
    }

    /**
     * 加载更多或者点击文档类
     */
    private void GetHomeData2() {
        LoaddingShow();
        String url = AppConst.BASE_URL + "choice/getjxfile";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("pg", String.valueOf(pg))
                .add("atype", atype)
                .add("scid", scid)
                .add("scid2", scid2)
                .add("keywords", keywords)
                .build();
        Request request = new Request
                .Builder()
                .addHeader("token", SharedPreferencesUtils.getString(getContext(), "token"))
                .url(url)//要访问的链接
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoaddingDismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LoaddingDismiss();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if ("1".equals(String.valueOf(status))) {
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.optJSONObject(i);
                            JXHomeModel jxHomeModel = new JXHomeModel();
                            jxHomeModel.setId(object1.optString("id"));
                            jxHomeModel.setImg(object1.optString("img"));
                            jxHomeModel.setName(object1.optString("name"));
                            jxHomeModel.setTag(object1.optString("tag"));
                            jxHomeModelList.add(jxHomeModel);

                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setNewData(jxHomeModelList);
                                mAdapter.notifyDataSetChanged();
                            }
                        });

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

    /**
     * 注册广播
     */
    public void register() {

        receiver = new MBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.select");
        getActivity().registerReceiver(receiver, filter);
    }

    /**
     * 定义广播
     */
    class MBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            scid = data.getString("scid");
            scid2 = data.getString("scid2");
            onRefresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tag = 0;
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);

        }
    }

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
//                        Looper.prepare();
//                        showToast(msg);
//                        Looper.loop();

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
