package com.fmapp.test01.adapter.provider.workstation;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.fmapp.test01.R;
import com.fmapp.test01.adapter.WorkStationAdapter;
import com.fmapp.test01.network.model.workStation.FolderModel;


public class FolderItemPrivider extends BaseItemProvider<FolderModel, BaseViewHolder> {
    @Override
    public int viewType() {
        return WorkStationAdapter.TYPE_FOLDER;
    }

    @Override
    public int layout() {
        return R.layout.cloud_files_item;
    }

    @Override
    public void convert(BaseViewHolder helper, FolderModel data, int position) {
        ImageView imageView = helper.getView(R.id.ivPic);
        imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_folder));
        helper.setText(R.id.tvName, data.getName());
        helper.setText(R.id.tvContent,  data.getIntime());
    }
}