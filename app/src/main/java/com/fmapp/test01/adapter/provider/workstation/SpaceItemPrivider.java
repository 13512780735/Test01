package com.fmapp.test01.adapter.provider.workstation;

import android.content.Intent;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.fmapp.test01.R;
import com.fmapp.test01.activity.cloud.LiveSpaceActivity;
import com.fmapp.test01.adapter.WorkStationAdapter;
import com.fmapp.test01.network.model.workStation.SpaceModel;
import com.fmapp.test01.utils.Utils;

public class SpaceItemPrivider extends BaseItemProvider<SpaceModel, BaseViewHolder> {
    @Override
    public int viewType() {
        return WorkStationAdapter.TYPE_SPACE;
    }

    @Override
    public int layout() {
        return R.layout.cloud_files_item;
    }

    @Override
    public void convert(BaseViewHolder helper, SpaceModel data, int position) {
        ImageView imageView = helper.getView(R.id.ivPic);
        imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_files));
        helper.setText(R.id.tvName, "永存空间");
        helper.setText(R.id.tvContent, data.getTotal() + "/" + data.getUsed());
    }

    @Override
    public void onClick(BaseViewHolder helper, SpaceModel data, int position) {
        super.onClick(helper, data, position);
        if (Utils.isFastClick()) {
        Intent intent = new Intent(mContext, LiveSpaceActivity.class);
        mContext.startActivity(intent);}
    }
}
