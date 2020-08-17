package com.fmapp.test01.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fmapp.test01.R;
import com.fmapp.test01.network.model.select.JXHomeModel;
import com.fmapp.test01.utils.ImageLoaderUtils;

import java.util.List;

public class SelectListAdapter extends BaseQuickAdapter<JXHomeModel, BaseViewHolder> {
    public SelectListAdapter(int layoutResId, @Nullable List<JXHomeModel> data) {
        super(R.layout.select_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JXHomeModel item) {
        ImageLoaderUtils.getInstance(mContext).displayImage(item.getImg(), helper.getView(R.id.ivAvatar));
        helper.setText(R.id.tvContent, item.getName());
        helper.setText(R.id.tvName, item.getTag());
    }
}
