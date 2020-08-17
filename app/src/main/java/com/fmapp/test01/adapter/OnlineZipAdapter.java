package com.fmapp.test01.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fmapp.test01.R;
import com.fmapp.test01.network.model.OnlineZipModel;

import java.util.List;

import static com.fmapp.test01.utils.com.GetHeaderImgById;

public class OnlineZipAdapter extends BaseQuickAdapter<OnlineZipModel, BaseViewHolder> {
    public OnlineZipAdapter(int layoutResId, @Nullable List<OnlineZipModel> data) {
        super(R.layout.zip_online_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OnlineZipModel item) {
        ImageView imageView = helper.getView(R.id.ivPic);
        imageView.setImageResource(GetHeaderImgById(item.getExt()));
        helper.setText(R.id.tvName, item.getName());
        helper.setText(R.id.tvContent, item.getSize());

    }
}
