package com.feemoo.fmapp.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.network.model.CouponModel;

import java.util.List;

public class CouponListAdapter extends BaseQuickAdapter<CouponModel.CouponBean, BaseViewHolder> {
    public CouponListAdapter(int layoutResId, @Nullable List<CouponModel.CouponBean> data) {
        super(R.layout.coupon_items, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponModel.CouponBean item) {
        ImageView ivAvatar = helper.getView(R.id.ivAvatar);
        if ("1".equals(item.getCtype())) {
            ivAvatar.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_coupon01));
        }
        if ("2".equals(item.getCtype())) {
            ivAvatar.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_coupon02));
        }
        helper.setText(R.id.tvName, item.getName());
        helper.setText(R.id.tvDate, item.getOtime());
    }
}
