package com.feemoo.fmapp.activity.MyInfo;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.login.RegisterProtocolActivity;
import com.feemoo.fmapp.adapter.VipLevelAdapter;
import com.feemoo.fmapp.adapter.VipPrivilegeAdapter;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.Appconst.AppConst;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.VipModel;
import com.feemoo.fmapp.network.model.VipPrivilegeModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.DensityUtil;
import com.feemoo.fmapp.utils.Utils;
import com.feemoo.fmapp.widght.BorderTextView;
import com.feemoo.fmapp.widght.CircleImageView;
import com.feemoo.fmapp.wxapi.JPayWx;
import com.gyf.immersionbar.ImmersionBar;
import com.jpay.alipay.JPay;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rx.Subscriber;

public class VipInfoActivity extends BaseActivity {
    @BindView(R.id.myScrollView)
    NestedScrollView myScrollView;

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.rl_header)
    RelativeLayout mRlHeader;
    @BindView(R.id.ll01)
    LinearLayout ll01;
    @BindView(R.id.iv01)
    CircleImageView mAvatar;
    @BindView(R.id.tvVip)
    BorderTextView mTvVip;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvIsVip)
    TextView tvIsVip;
    @BindView(R.id.protocol_tv)
    TextView mTvProtocol;
    @BindView(R.id.payWay_choose)
    RadioGroup mRadioGroup;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleViewFree;
    @BindView(R.id.mRecycleViewVip)
    RecyclerView mRecycleViewVip;
    @BindView(R.id.tvConfirm)
    BorderTextView tvConfirm;
    @BindView(R.id.checkbox01)
    CheckBox checkBox;
    // 设置适配器的图片资源
    int[] imageId = new int[]{R.mipmap.icon_wangyejisuxiazai,
            R.mipmap.icon_appjisuxiazai, R.mipmap.icon_kefuduanjisuxiazai,
            R.mipmap.icon_mianguanggao, R.mipmap.icon_zaixianjieyasuo,
            R.mipmap.icon_zaixianyulan, R.mipmap.icon_dengjichengzhang, R.mipmap.icon_zhuanshuguajian};
    // 设置标题
    String[] title = new String[]{"网页极速下载", "APP极速下载", "客户端极速下载", "免广告/免等待",
            "在线解压缩", "在线预览", "等级成长加速", "专属挂件"};
    List<VipPrivilegeModel> listitem = new ArrayList<>();
    List<VipModel.SvipsBean> svipsBeans = new ArrayList<>();
    private VipPrivilegeAdapter mPrivilegeAdapter;
    private VipModel vipModel;
    private VipLevelAdapter mVipLevelAdapter;
    private String payFlag = "0";
    private String sVipId;
    private String paytp = "alipay";

    /**
     * 微信数据
     *
     * @param savedInstanceState
     */
    String prepayid;
    String appid;
    String partnerid;
    String packages;
    String noncestr;
    String timestamp;
    String sign;
    //支付宝
    private String payUrl;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_info);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        initUI();
        initData();
        initVipInfo();
    }

    private void initVipInfo() {
        LoaddingShow();
        RetrofitUtil.getInstance().vipinfo(token, new Subscriber<BaseResponse<VipModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoaddingDismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    showToast(resultException.getMsg());
                }
            }

            @Override
            public void onNext(BaseResponse<VipModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    vipModel = baseResponse.getData();
                    refresh(vipModel);

                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    private void refresh(VipModel vipModel) {
        if (vipModel.getUser() != null) {
            Glide.with(mContext).load(vipModel.getUser().getUlogo()).into(mAvatar);
            mTvVip.setText(vipModel.getUser().getSlevel());
            tvName.setText(vipModel.getUser().getUname());
            if ("yes".equals(vipModel.getUser().getIssvip())) {
                tvIsVip.setVisibility(View.VISIBLE);
                tvIsVip.setText("终身会员");
            } else {
                tvIsVip.setVisibility(View.GONE);
            }

        }
        if (vipModel.getSvips() != null) {
            List<VipModel.SvipsBean> svipsBeans1 = vipModel.getSvips();
            for (int i = 0; i < svipsBeans1.size(); i++) {
                if (String.valueOf(svipsBeans1.get(i).getVipid()).equals("1004")) {
                    VipModel.SvipsBean svipsBean = new VipModel.SvipsBean();
                    svipsBean.setOprice(svipsBeans1.get(i).getOprice());
                    svipsBean.setPrice(svipsBeans1.get(i).getPrice());
                    svipsBean.setName("终身飞猫+鲸选");
                    svipsBean.setImg(R.mipmap.icon_baoxiang);
                    svipsBean.setUnit(svipsBeans1.get(i).getUnit());
                    svipsBean.setVipid(svipsBeans1.get(i).getVipid());
                    svipsBeans.add(svipsBean);
                }
                if (String.valueOf(svipsBeans1.get(i).getVipid()).equals("1003")) {
                    VipModel.SvipsBean svipsBean = new VipModel.SvipsBean();
                    svipsBean.setOprice(svipsBeans1.get(i).getOprice());
                    svipsBean.setPrice(svipsBeans1.get(i).getPrice());
                    svipsBean.setName("1年");
                    svipsBean.setImg(R.mipmap.icon_jinxunzhang);
                    svipsBean.setUnit(svipsBeans1.get(i).getUnit());
                    svipsBean.setVipid(svipsBeans1.get(i).getVipid());
                    svipsBeans.add(svipsBean);
                }
                if (String.valueOf(svipsBeans1.get(i).getVipid()).equals("1002")) {
                    VipModel.SvipsBean svipsBean = new VipModel.SvipsBean();
                    svipsBean.setOprice(svipsBeans1.get(i).getOprice());
                    svipsBean.setPrice(svipsBeans1.get(i).getPrice());
                    svipsBean.setName("3个月");
                    svipsBean.setImg(R.mipmap.icon_yinxunzhang);
                    svipsBean.setUnit(svipsBeans1.get(i).getUnit());
                    svipsBean.setVipid(svipsBeans1.get(i).getVipid());
                    svipsBeans.add(svipsBean);
                }
                if (String.valueOf(svipsBeans1.get(i).getVipid()).equals("1001")) {
                    VipModel.SvipsBean svipsBean = new VipModel.SvipsBean();
                    svipsBean.setOprice(svipsBeans1.get(i).getOprice());
                    svipsBean.setName("1个月");
                    svipsBean.setImg(R.mipmap.icon_tongxunzhang);
                    svipsBean.setPrice(svipsBeans1.get(i).getPrice());
                    svipsBean.setUnit(svipsBeans1.get(i).getUnit());
                    svipsBean.setVipid(svipsBeans1.get(i).getVipid());
                    svipsBeans.add(svipsBean);
                }
            }
            Collections.sort(svipsBeans);
            mVipLevelAdapter.notifyDataSetChanged();
            mVipLevelAdapter.setPosition(0);
            sVipId = String.valueOf(svipsBeans.get(0).getVipid());
            mVipLevelAdapter.setOnItemClickListener(new VipLevelAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, VipModel.SvipsBean s) {
                    mVipLevelAdapter.setPosition(position); //传递当前的点击位置
                    mVipLevelAdapter.notifyDataSetChanged(); //通知刷新
                    sVipId = String.valueOf(svipsBeans.get(position).getVipid());
                }
            });
        }
    }

    private void initData() {
        // 将上述资源转化为list集合
        for (int i = 0; i < title.length; i++) {
            VipPrivilegeModel vipPrivilegeModel = new VipPrivilegeModel();
            vipPrivilegeModel.setImg(imageId[i]);
            vipPrivilegeModel.setName(title[i]);
            listitem.add(vipPrivilegeModel);
        }
        mPrivilegeAdapter.setNewData(listitem);
        mPrivilegeAdapter.notifyDataSetChanged();

    }

    private void initUI() {
        mToolbar.setAlpha(0);
        Drawable drawable = getResources().getDrawable(
                R.mipmap.icon_back);
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        int height01 = width;//屏幕高度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlHeader.getLayoutParams();
        params.width = width;
        params.height = height01 - 100;
        mRlHeader.setLayoutParams(params);//设置配置参数
        myScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            int height = DensityUtil.dip2px(mContext, 140);
            if (i1 <= 0) {
                mTvTitle.setText("开通SVIP会员");
                mToolbar.setAlpha(0);
                mRlHeader.setVisibility(View.VISIBLE);
            } else if (i1 > 0 && i1 < height) {
                mTvTitle.setText("开通SVIP会员");
                //获取渐变率
                float scale = (float) i1 / height;
                //获取渐变数值
                float alpha = (1.0f * scale);
                mToolbar.setAlpha(alpha);
                mRlHeader.setVisibility(View.INVISIBLE);
            } else {
                mToolbar.setAlpha(1f);
            }

        });

        String str1 = "我已阅读并同意报考<font color='#FBC02D'>“飞慕飞猫云超级会员服务一经购买不予退款。”</font>在内的" +
                "<font color='#326ef3'>《飞慕飞猫云付费会员服务协议》</font>之全部内容";
        mTvProtocol.setText(Html.fromHtml(str1));
        mRecycleViewFree.setLayoutManager(new GridLayoutManager(this,
                4));
        mPrivilegeAdapter = new VipPrivilegeAdapter(R.layout.layout_privilege_item, listitem);
        mRecycleViewFree.setAdapter(mPrivilegeAdapter);
        mPrivilegeAdapter.notifyDataSetChanged();
        mRecycleViewVip.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mVipLevelAdapter = new VipLevelAdapter(mContext, svipsBeans);
        mRecycleViewVip.setAdapter(mVipLevelAdapter);
        mVipLevelAdapter.notifyDataSetChanged();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (!checkBox.isChecked()) {
            showToast("请您仔细阅读并且同意会员付费协议");
            return;
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case R.id.alipay:
                        payFlag = "0";
                        paytp = "alipay";
                        break;
                    case R.id.weChat:
                        payFlag = "1";
                        break;
                    case R.id.huabei:
                        payFlag = "2";
                        paytp = "huabei";
                        break;
                }
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFastClick()) {
                    if (!checkBox.isChecked()) {
                        showToast("请您仔细阅读并且同意会员付费协议");
                        return;
                    }
                    if ("0".equals(payFlag)) {
                        toAlipay(sVipId, paytp);
                    }
                    if ("1".equals(payFlag)) {
                        toWxsign(sVipId);
                    }
                    if ("2".equals(payFlag)) {
                        toAlipay(sVipId, paytp);
                    }
                }
            }
        });
        mTvProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = AppConst.BASE_URL01 + "archives/128";
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                bundle.putString("title", "SVIP会员服务协议");
                toActivity(RegisterProtocolActivity.class, bundle);
            }
        });

    }

    /**
     * 支付宝或花呗
     *
     * @param sVipId
     * @param paytp
     */
    private void toAlipay(String sVipId, String paytp) {
        LoaddingShow();
        String url = AppConst.BASE_URL + "api/alipayappv2";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("vip_id", sVipId)
                .add("pakid", "")
                .add("paytp", paytp)
                .build();
        Request request = new Request
                .Builder()
                .url(url)//要访问的链接
                .addHeader("token", token)
                .post(formBody)
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
                        payUrl = object.optString("data");
                        showAlipay(payUrl);
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

    private void showAlipay(String payUrl) {
        JPay.getIntance(this).toAliPay(payUrl, new JPay.AliPayListener() {
            @Override
            public void onPaySuccess() {
                showToast("支付成功");
            }

            @Override
            public void onPayError(int error_code, String message) {
                showToast("支付失败");
            }

            @Override
            public void onPayCancel() {
                showToast("取消了支付");
            }
        });
    }


    /**
     * 微信
     *
     * @param sVipId
     */
    private void toWxsign(String sVipId) {
        LoaddingShow();
        String url = AppConst.BASE_URL + "api/wxsign";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("vip_id", sVipId)
                .add("pakid", "")
                .build();
        Request request = new Request
                .Builder()
                .url(url)//要访问的链接
                .addHeader("token", token)
                .post(formBody)
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
                // Log.d("数据微信：", response.body().string());
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if (status == 1) {
                        prepayid = object.optJSONObject("data").optString("prepayid");
                        appid = object.optJSONObject("data").optString("appid");
                        partnerid = object.optJSONObject("data").optString("partnerid");
                        packages = object.optJSONObject("data").optString("package");
                        noncestr = object.optJSONObject("data").optString("noncestr");
                        timestamp = object.optJSONObject("data").optString("timestamp");
                        sign = object.optJSONObject("data").optString("sign");
                        showWeChat(prepayid, appid, partnerid, packages, noncestr, timestamp, sign);
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
     * 调起微信支付
     *
     * @param prepayid
     * @param appid
     * @param partnerid
     * @param packages
     * @param noncestr
     * @param timestamp
     * @param sign
     */
    private void showWeChat(String prepayid, String appid, String partnerid, String packages, String noncestr, String timestamp, String sign) {
        Log.d("appid:", appid);
        api = WXAPIFactory.createWXAPI(VipInfoActivity.this, null);

        if (isWxAppInstalledAndSupported(mContext)) {
            // 将该app注册到微信
            JPayWx.getIntance(this).toWxPay(appid, partnerid, prepayid, noncestr, timestamp, sign, packages, new JPayWx.WxPayListener() {
                @Override
                public void onPaySuccess() {
                    showToast("支付成功");
                }

                @Override
                public void onPayError(int error_code, String message) {
                    showToast("支付失败");
                }

                @Override
                public void onPayCancel() {
                    showToast("取消了支付");
                }
            });
        } else {
            Looper.prepare();
            showToast("请安装好微信应用在支付");
            Looper.loop();
            return;
        }


    }

//    /**
//     * 调起支付宝页面
//     *
//     * @param payUrl
//     */
//    private void showAlipay(String payUrl) {
//        final String orderInfo = payUrl;
//
//        Runnable payRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(VipInfoActivity.this);
//                Map<String, String> result = alipay.payV2(orderInfo, true);
//                Message msg = new Message();
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//        // 必须异步调用
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
////
////            /**
////             * 参数解释
////             *
////             * @param status 是结果码(类型为字符串)。
////             *       9000	订单支付成功
////             *       8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
////             *       4000	订单支付失败
////             *       5000	重复请求
////             *       6001	用户中途取消
////             *       6002	网络连接出错
////             *       6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
////             *       其它	其它支付错误
////             * @param json        是处理结果(类型为json结构字符串)
////             *       out_trade_no	String	是	64	商户网站唯一订单号	70501111111S001111119
////             *       trade_no	String	是	64	该交易在支付宝系统中的交易流水号。最长64位。	2014112400001000340011111118
////             *       app_id	String	是	32	支付宝分配给开发者的应用Id。	2014072300007148
////             *       total_amount	Price	是	9	该笔订单的资金总额，单位为RMB-Yuan。取值范围为[0.01,100000000.00]，精确到小数点后两位。	9.00
////             *       seller_id	String	是	16	收款支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字	2088111111116894
////             *       msg	String	是	16	处理结果的描述，信息来自于code返回结果的描述	success
////             *       charset	String	是	16	编码格式	utf-8
////             *       timestamp	String	是	32	时间	2016-10-11 17:43:36
////             *       code	String	是	16	结果码	具体见
//    }

    /**
     * 判断手机是否安装微信
     *
     * @param context 上下文
     * @return
     */
    public static boolean isWxAppInstalledAndSupported(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }
//
//    private Handler mHandler = new Handler() {
//
//        public void handleMessage(Message msg) {
//            Result payResult = new Result((Map<String, String>) msg.obj);
//            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//            String resultStatus = payResult.getResultStatus();
//            // 判断resultStatus 为9000则代表支付成功
//            if (TextUtils.equals(resultStatus, "9000")) {
//                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                showToast("支付成功");
//            } else if (TextUtils.equals(resultStatus, "6001")) {
//                showToast("支付取消");
//            } else {
//                showToast("支付失败");
//            }
//
//        }
//    };
}
