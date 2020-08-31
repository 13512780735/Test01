package com.feemoo.fmapp.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.network.model.JifenShopModel;

import java.util.List;

public class JiFenShopAdapter extends BaseQuickAdapter<JifenShopModel, BaseViewHolder> {
    public JiFenShopAdapter(int layoutResId, @Nullable List<JifenShopModel> data) {
        super(R.layout.qiandao_shop_items, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JifenShopModel item) {
        ImageView imageView = helper.getView(R.id.ivAvatar01);
        imageView.setImageResource(item.getUrl());
        helper.setText(R.id.tvName01, item.getName());
        helper.setText(R.id.tvNum01, item.getContent());
        helper.setText(R.id.tvjifen01, item.getNum());
        helper.addOnClickListener(R.id.tvUse01);

    }
}
