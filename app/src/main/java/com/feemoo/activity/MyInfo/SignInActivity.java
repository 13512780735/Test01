package com.feemoo.activity.MyInfo;


import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feemoo.R;
import com.feemoo.adapter.JiFenGoodsAdapter;
import com.feemoo.adapter.JiFenShopAdapter;
import com.feemoo.adapter.JiFenSignAdapter;
import com.feemoo.base.BaseActivity;
import com.feemoo.network.Appconst.AppConst;
import com.feemoo.network.model.BaseResponse;
import com.feemoo.network.model.JiFenGoodsModel;
import com.feemoo.network.model.JiFenModel;
import com.feemoo.network.model.JiFenSignModel;
import com.feemoo.network.model.JifenShopModel;
import com.feemoo.network.util.DataResultException;
import com.feemoo.network.util.RetrofitUtil;
import com.feemoo.utils.CustomDialog;
import com.feemoo.widght.BorderTextView;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rx.Subscriber;

public class SignInActivity extends BaseActivity {
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.tvPoint)
    TextView tvPoint;
    @BindView(R.id.rlqiandao)
    RelativeLayout rlqiandao;
    @BindView(R.id.tvqiandao)
    TextView tvqiandao;
    @BindView(R.id.mRecycleViewShop)
    RecyclerView mRecycleViewShop;
    @BindView(R.id.mRecycleViewGoods)
    RecyclerView mRecycleViewGoods;
    @BindView(R.id.mRecycleViewSign)
    RecyclerView mRecycleViewSign;
    List<JifenShopModel> listShop = new ArrayList<>();
    List<JiFenModel.TasksBean> tasksBeans = new ArrayList<>();
    List<JiFenGoodsModel> jiFenGoodsModels = new ArrayList<>();
    List<JiFenSignModel> jiFenSignModels = new ArrayList<>();
    private JifenShopModel jifenShopModel;
    private JiFenShopAdapter mJiFenShopAdapter;
    private JiFenGoodsModel jiFenGoodsModel;
    private JiFenGoodsAdapter mJiFenGoodsAdapter;
    private JiFenSignAdapter mJiFenSignAdapter;
    private JiFenSignModel jiFenSignModel;
    private BorderTextView tvConfirm;
    private String point;
    private int loop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initUI();
        initData();
    }

    private void initData() {
        LoaddingShow();
        RetrofitUtil.getInstance().getsigndayv2(token, new Subscriber<BaseResponse<JiFenModel>>() {
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
            public void onNext(BaseResponse<JiFenModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    JiFenModel jiFenModel = baseResponse.getData();
                    refresh(jiFenModel);
                }
            }
        });

    }

    private void refresh(JiFenModel jiFenModel) {
        point = String.valueOf(jiFenModel.getPoint());
        tvPoint.setText(point);
        getShopData(jiFenModel);
        getGoodsData(jiFenModel);
        loop = jiFenModel.getLoop();
        getSignData(loop);
        String ifsign = String.valueOf(jiFenModel.getIfsign());
        if ("0".equals(ifsign)) {
            rlqiandao.setClickable(true);
            rlqiandao.setBackground(getResources().getDrawable(R.mipmap.icon_qiandao01));
            tvqiandao.setTextColor(getResources().getColor(R.color.white));
            tvqiandao.setText("签到领积分");

        } else {
            rlqiandao.setClickable(false);
            rlqiandao.setBackground(getResources().getDrawable(R.mipmap.icon_qiandao02));
            tvqiandao.setText("已经连续签到" + jiFenModel.getSigcount() + "天");
            tvqiandao.setTextColor(getResources().getColor(R.color.text_color));
        }
        rlqiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSign(jiFenSignModels.get(loop).getNum());

            }
        });
    }

    String jifenData[] = {"5", "5", "5", "5", "5", "10", "20"};

    /**
     * 获取签到天数
     *
     * @param loop
     */
    private void getSignData(int loop) {
        //   int Loop = jiFenModel.getLoop();
        for (int i = 0; i < jifenData.length; i++) {
            jiFenSignModel = new JiFenSignModel();
            jiFenSignModel.setDay(String.valueOf(i + 1) + "天");
            jiFenSignModel.setNum(jifenData[i]);
            if (i < loop) {
                jiFenSignModel.setCheck(true);
            } else {
                jiFenSignModel.setCheck(false);
            }

            jiFenSignModels.add(jiFenSignModel);
        }
        mJiFenSignAdapter.setNewData(jiFenSignModels);
        mJiFenSignAdapter.notifyDataSetChanged();
    }

    /**
     * 积分商品
     *
     * @param jiFenModel
     */
    private void getGoodsData(JiFenModel jiFenModel) {
        if (jiFenModel.getTasks().size() > 0) {
            tasksBeans = jiFenModel.getTasks();
            for (int i = 0; i < tasksBeans.size(); i++) {
                if (tasksBeans.get(i).getTid().equals("4")) {
                    jiFenGoodsModel = new JiFenGoodsModel();
                    jiFenGoodsModel.setId(tasksBeans.get(i).getId());
                    jiFenGoodsModel.setTid(tasksBeans.get(i).getTid());
                    jiFenGoodsModel.setTstatus(tasksBeans.get(i).getTstatus());
                    jiFenGoodsModel.setName("绑定手机");
                    jiFenGoodsModel.setUrl(R.mipmap.icon_jifen04);
                    jiFenGoodsModel.setContent("绑定手机后即可领取 +10积分");
                    jiFenGoodsModel.setNum("+10积分");
                    jiFenGoodsModels.add(0, jiFenGoodsModel);
                } else if (tasksBeans.get(i).getTid().equals("5")) {
                    jiFenGoodsModel = new JiFenGoodsModel();
                    jiFenGoodsModel.setId(tasksBeans.get(i).getId());
                    jiFenGoodsModel.setTid(tasksBeans.get(i).getTid());
                    jiFenGoodsModel.setTstatus(tasksBeans.get(i).getTstatus());
                    jiFenGoodsModel.setName("添加提现信息");
                    jiFenGoodsModel.setUrl(R.mipmap.icon_jifen05);
                    jiFenGoodsModel.setContent("在网页端添加提现信息 +10积分");
                    jiFenGoodsModel.setNum("+10积分");
                    jiFenGoodsModels.add(1, jiFenGoodsModel);
                } else if (tasksBeans.get(i).getTid().equals("6")) {
                    jiFenGoodsModel = new JiFenGoodsModel();
                    jiFenGoodsModel.setId(tasksBeans.get(i).getId());
                    jiFenGoodsModel.setTid(tasksBeans.get(i).getTid());
                    jiFenGoodsModel.setTstatus(tasksBeans.get(i).getTstatus());
                    jiFenGoodsModel.setName("在鲸选投稿成功");
                    jiFenGoodsModel.setUrl(R.mipmap.icon_jifen06);
                    jiFenGoodsModel.setContent("在网页端投稿鲸选文件 +20积分");
                    jiFenGoodsModel.setNum("+20积分");
                    jiFenGoodsModels.add(2, jiFenGoodsModel);
                } else if (tasksBeans.get(i).getTid().equals("7")) {
                    jiFenGoodsModel = new JiFenGoodsModel();
                    jiFenGoodsModel.setId(tasksBeans.get(i).getId());
                    jiFenGoodsModel.setTid(tasksBeans.get(i).getTid());
                    jiFenGoodsModel.setTstatus(tasksBeans.get(i).getTstatus());
                    jiFenGoodsModel.setName("每连续签到10天");
                    jiFenGoodsModel.setNum("+20积分");
                    jiFenGoodsModel.setUrl(R.mipmap.icon_jifen07);
                    jiFenGoodsModel.setContent("连续签到10天 +20积分");
                    jiFenGoodsModels.add(3, jiFenGoodsModel);
                } else if (tasksBeans.get(i).getTid().equals("8")) {
                    jiFenGoodsModel = new JiFenGoodsModel();
                    jiFenGoodsModel.setId(tasksBeans.get(i).getId());
                    jiFenGoodsModel.setTid(tasksBeans.get(i).getTid());
                    jiFenGoodsModel.setTstatus(tasksBeans.get(i).getTstatus());
                    jiFenGoodsModel.setName("每连续签到30天");
                    jiFenGoodsModel.setNum("+50积分");
                    jiFenGoodsModel.setUrl(R.mipmap.icon_jifen08);
                    jiFenGoodsModel.setContent("连续签到30天 +50积分");
                    jiFenGoodsModels.add(4, jiFenGoodsModel);
                } else if (tasksBeans.get(i).getTid().equals("9")) {
                    jiFenGoodsModel = new JiFenGoodsModel();
                    jiFenGoodsModel.setId(tasksBeans.get(i).getId());
                    jiFenGoodsModel.setTid(tasksBeans.get(i).getTid());
                    jiFenGoodsModel.setTstatus(tasksBeans.get(i).getTstatus());
                    jiFenGoodsModel.setName("下载1次鲸选文件");
                    jiFenGoodsModel.setNum("+2积分");
                    jiFenGoodsModel.setUrl(R.mipmap.icon_jifen09);
                    jiFenGoodsModel.setContent("下载鲸选文件 +2积分");
                    jiFenGoodsModels.add(5, jiFenGoodsModel);
                }
                mJiFenGoodsAdapter.setNewData(jiFenGoodsModels);
                mJiFenGoodsAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 积分商城
     *
     * @param jiFenModel
     */
    private void getShopData(JiFenModel jiFenModel) {
        jifenShopModel = new JifenShopModel();
        jifenShopModel.setName("单次SVIP下载券");
        jifenShopModel.setUrl(R.mipmap.icon_coupon01);
        jifenShopModel.setNum("20积分");
        jifenShopModel.setContent("每天限量" + jiFenModel.getSdown() + "张(三天有效期)");
        jifenShopModel.setTid("1");
        listShop.add(0, jifenShopModel);
        jifenShopModel = new JifenShopModel();
        jifenShopModel.setName("一天SVIP体验");
        jifenShopModel.setUrl(R.mipmap.icon_coupon03);
        jifenShopModel.setTid("2");
        jifenShopModel.setNum("200积分");
        jifenShopModel.setContent("每天限量" + jiFenModel.getOsvip() + "张");
        listShop.add(1, jifenShopModel);
        jifenShopModel = new JifenShopModel();
        jifenShopModel.setName("20元SVIP代金券");
        jifenShopModel.setUrl(R.mipmap.icon_coupon02);
        jifenShopModel.setNum("10积分");
        jifenShopModel.setContent("限定购买年度会员");
        listShop.add(2, jifenShopModel);
        jifenShopModel.setTid("3");
        mJiFenShopAdapter.setNewData(listShop);
        mJiFenShopAdapter.notifyDataSetChanged();
    }

    private void initUI() {
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        /**
         * 积分商城
         */
        mRecycleViewShop.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        mJiFenShopAdapter = new JiFenShopAdapter(R.layout.qiandao_shop_items, listShop);
        mRecycleViewShop.setAdapter(mJiFenShopAdapter);
        mJiFenShopAdapter.notifyDataSetChanged();
        mJiFenShopAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String jifen = listShop.get(position).getNum();
                String tid = listShop.get(position).getTid();
                showDialog01(jifen, tid);
            }
        });
        /**
         * 积分商品
         */
        mRecycleViewGoods.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        mJiFenGoodsAdapter = new JiFenGoodsAdapter(R.layout.jifen_goods_item, jiFenGoodsModels);
        mRecycleViewGoods.setAdapter(mJiFenGoodsAdapter);
        mJiFenGoodsAdapter.notifyDataSetChanged();
        mJiFenGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LinearLayout layout = (LinearLayout) view.getParent();
                tvConfirm = layout.findViewById(R.id.tvConfirm);
                String tid = jiFenGoodsModels.get(position).getTid();
                String Tstatus = jiFenGoodsModels.get(position).getTstatus();
                if ("1".equals(Tstatus)) {
                    toTask(tid);
                    tvConfirm.setContentColorResource01(mContext.getResources().getColor(R.color.gray_background_color));
                    tvConfirm.setTextColor(mContext.getResources().getColor(R.color.text_color));
                    tvConfirm.setStrokeWidth(0);
                    tvConfirm.setText("已领取");
                } else {
                }


            }
        });
        /**
         * 签到
         */
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRecycleViewSign.getLayoutParams();
        params.width = width - 40;
        mRecycleViewSign.setLayoutParams(params);//设置配置参数

        //mRecycleViewSign
        mRecycleViewSign.setLayoutManager(new GridLayoutManager(mContext,
                7));
        mJiFenSignAdapter = new JiFenSignAdapter(R.layout.jifen_sign_item, jiFenSignModels);
        mRecycleViewSign.setAdapter(mJiFenSignAdapter);
        mJiFenSignAdapter.notifyDataSetChanged();

    }

    /**
     * 积分兑换
     *
     * @param tid
     * @param jifen
     */
    CustomDialog dialog;

    private void showDialog01(String jifen, String tid) {
        dialog = new CustomDialog(this).builder()
                .setGravity(Gravity.CENTER)
                .setTitle("确认兑换", getResources().getColor(R.color.black))//可以不设置标题颜色，默认系统颜色
                .setSubTitle("本次兑换需要消耗" + jifen)

                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toExchange(tid);
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    /**
     * 兑换商品
     *
     * @param tid
     */
    private void toExchange(String tid) {
        LoaddingShow();
        String url = AppConst.BASE_URL + "user/excg";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("tid", tid)
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                point = object.optJSONObject("data").optString("point");
                                tvPoint.setText(point);
                            }
                        });

                        Looper.prepare();
                        showToast(msg);
                        Looper.loop();

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
     * 签到
     *
     * @param num
     */
    private void toSign(String num) {
        LoaddingShow();
        String url = AppConst.BASE_URL + "user/tosignv2";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
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
                //{"msg":"\u7b7e\u5230\u6210\u529f","status":1,"data":{"loop":1,"sigcount":1,"add":5,"point":485}}
                //  Log.d("签到数据：", response.body().string());
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if (status == 1) {
                        point = object.optJSONObject("data").optString("point");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvPoint.setText(point);
                                loop = object.optJSONObject("data").optInt("loop");
                                int sigcount = object.optJSONObject("data").optInt("sigcount");
                                getSignData(loop);
                                rlqiandao.setClickable(false);
                                rlqiandao.setBackground(getResources().getDrawable(R.mipmap.icon_qiandao02));
                                tvqiandao.setTextColor(getResources().getColor(R.color.text_color));
                                tvqiandao.setText("已经连续签到" + sigcount + "天");
                                showSignCenterDialog showSignCenterDialog = new showSignCenterDialog();
                                showSignCenterDialog.CenterDialog(mContext, num);
                            }
                        });

                  /*      Looper.prepare();
                        showToast(msg);
                        Looper.loop();*/

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
     * 完成任务领取积分
     */
    private void toTask(String tid) {
        LoaddingShow();
        String url = AppConst.BASE_URL + "user/totask";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("tid", tid)
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
                Log.d("积分数据：", response.body().string());
                //{"msg":"\u9886\u53d6\u6210\u529f","status":1,"data":{"add":50}}
                LoaddingDismiss();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if (status == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                point = String.valueOf(Integer.valueOf(point) + Integer.parseInt(object.optJSONObject("data").optString("add")));
                                tvPoint.setText(point);
                            }
                        });

                        Looper.prepare();
                        showToast(msg);
                        Looper.loop();
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
