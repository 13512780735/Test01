package com.feemoo.fmapp.activity.MyInfo;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.adapter.VipPrivilegeAdapter;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.VipModel;
import com.feemoo.fmapp.network.model.VipPrivilegeModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.DensityUtil;
import com.feemoo.fmapp.widght.BorderTextView;
import com.feemoo.fmapp.widght.CircleImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class VipInfoActivity extends BaseActivity {
    @BindView(R.id.myScrollView)
    NestedScrollView myScrollView;
    @BindView(R.id.titleBgRl)
    RelativeLayout titleBgRl;
    @BindView(R.id.rl_header)
    RelativeLayout mRlHeader;
    @BindView(R.id.tv_title_text)
    TextView mTitle;
    @BindView(R.id.btn_title_left)
    TextView btn_title_left;
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
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleViewFree;
    @BindView(R.id.mRecycleViewVip)
    RecyclerView mRecycleViewVip;
    // 设置适配器的图片资源
    int[] imageId = new int[]{R.mipmap.icon_wangyejisuxiazai,
            R.mipmap.icon_appjisuxiazai, R.mipmap.icon_kefuduanjisuxiazai,
            R.mipmap.icon_mianguanggao, R.mipmap.icon_zaixianjieyasuo,
            R.mipmap.icon_zaixianyulan, R.mipmap.icon_dengjichengzhang, R.mipmap.icon_zhuanshuguajian};

    // 设置标题
    String[] title = new String[]{"网页极速下载", "APP极速下载", "客户端极速下载", "免广告/免等待",
            "在线解压缩", "在线预览", "等级成长加速", "专属挂件"};
    List<VipPrivilegeModel> listitem = new ArrayList<>();
    private VipPrivilegeAdapter mPrivilegeAdapter;
    private VipModel vipModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_info);
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
    }

    private void initData() {
        // 将上述资源转化为list集合
        for (
                int i = 0; i < title.length; i++) {
            VipPrivilegeModel vipPrivilegeModel = new VipPrivilegeModel();
            vipPrivilegeModel.setImg(imageId[i]);
            vipPrivilegeModel.setName(title[i]);
            listitem.add(vipPrivilegeModel);
        }
        mPrivilegeAdapter.setNewData(listitem);
        mPrivilegeAdapter.notifyDataSetChanged();
    }

    private void initUI() {
        Drawable drawable = getResources().getDrawable(
                R.mipmap.icon_back);
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        int height01 = width;//屏幕高度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlHeader.getLayoutParams();
        params.width = width;
        params.height = height01;
        mRlHeader.setLayoutParams(params);//设置配置参数
        myScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            int height = DensityUtil.dip2px(mContext, 50);
            if (i1 <= 0) {
                mTitle.setText("开通SVIP会员");
                mTitle.setAlpha(0);
                btn_title_left.setAlpha(0);
                mRlHeader.setVisibility(View.VISIBLE);
            } else if (i1 > 0 && i1 < height) {
                mTitle.setText("开通SVIP会员");
                //获取渐变率
                float scale = (float) i1 / height;
                //获取渐变数值
                float alpha = (1.0f * scale);
                mTitle.setAlpha(alpha);
                btn_title_left.setAlpha(alpha);
                mRlHeader.setVisibility(View.INVISIBLE);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                        drawable.getMinimumHeight());
                btn_title_left.setCompoundDrawables(drawable, null, null, null);
            } else {
                mTitle.setAlpha(1f);
                btn_title_left.setAlpha(1f);
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
//        mPrivilegeAdapter = new VipPrivilegeAdapter(R.layout.layout_privilege_item, listitem);
//        mRecycleViewVip.setAdapter(mPrivilegeAdapter);
//        mPrivilegeAdapter.notifyDataSetChanged();
    }
}
