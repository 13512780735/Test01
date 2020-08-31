package com.feemoo.fmapp.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.select.ProjectDetailsActivity;
import com.feemoo.fmapp.network.model.select.JXHomeModel;
import com.feemoo.fmapp.utils.ImageLoaderUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SelectListAdapter extends BaseQuickAdapter<JXHomeModel, BaseViewHolder> {
    public SelectListAdapter(int layoutResId, @Nullable List<JXHomeModel> data) {
        super(R.layout.select_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JXHomeModel item) {
        RoundedImageView imageView01 = (RoundedImageView) helper.getView(R.id.ivAvatar);
        if (item.getImg() == null) {
            //  (RoundedImageView)helper.getView(R.id.ivAvatar).setBackgroundResource(mContext.getResources().getDrawable(R.mipmap.icon_logo);
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.mipmap.icon_logo)).into(imageView01);
        } else {
            Glide.with(mContext).load(item.getImg()).into(imageView01);
        }
        helper.setText(R.id.tvContent, item.getName());
        helper.setText(R.id.tvName, item.getTag());
        helper.getView(R.id.ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", item.getId());
                Intent intent = new Intent(mContext, ProjectDetailsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
