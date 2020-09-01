package com.feemoo.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feemoo.R;
import com.feemoo.network.model.JiFenGoodsModel;
import com.feemoo.widght.BorderTextView;

import java.util.List;

public class JiFenGoodsAdapter extends BaseQuickAdapter<JiFenGoodsModel, BaseViewHolder> {
    public JiFenGoodsAdapter(int layoutResId, @Nullable List<JiFenGoodsModel> data) {
        super(R.layout.jifen_goods_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JiFenGoodsModel item) {
        ImageView imageView = helper.getView(R.id.ivAvatar);
        BorderTextView tvConfirm = helper.getView(R.id.tvConfirm);
        imageView.setImageResource(item.getUrl());
        helper.setText(R.id.tvName, item.getName());
        helper.setText(R.id.tvContent, item.getContent());
        String tstatus = item.getTstatus();
        if ("0".equals(tstatus)) {
            tvConfirm.setContentColorResource01(mContext.getResources().getColor(R.color.jifen_qiandao01));
            tvConfirm.setTextColor(mContext.getResources().getColor(R.color.white));
            tvConfirm.setStrokeWidth(0);
            tvConfirm.setText(item.getNum());
        } else if ("1".equals(tstatus)) {
            tvConfirm.setContentColorResource01(mContext.getResources().getColor(R.color.white));
            tvConfirm.setTextColor(mContext.getResources().getColor(R.color.jifen_qiandao01));
            tvConfirm.setStrokeColor01(mContext.getResources().getColor(R.color.jifen_qiandao01));
            tvConfirm.setStrokeWidth(1);
            tvConfirm.setText("领取积分");
        } else if ("2".equals(tstatus)) {
            tvConfirm.setContentColorResource01(mContext.getResources().getColor(R.color.gray_background_color));
            tvConfirm.setTextColor(mContext.getResources().getColor(R.color.text_color));
            tvConfirm.setStrokeWidth(0);
            tvConfirm.setText("已领取");
        }
        helper.addOnClickListener(R.id.tvConfirm);
    }
}
