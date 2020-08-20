package com.feemoo.fmapp.adapter.provider.workstation;

import android.content.Intent;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.file.work.workFilePreviewActivity;
import com.feemoo.fmapp.adapter.WorkStationAdapter;
import com.feemoo.fmapp.network.model.workStation.FolderModel;
import com.feemoo.fmapp.utils.Utils;


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

    @Override
    public void onClick(BaseViewHolder helper, FolderModel data, int position) {
        if (Utils.isFastClick()) {
        Intent intent=new Intent(mContext, workFilePreviewActivity.class);
        intent.putExtra("name",data.getName());
        intent.putExtra("id",data.getId());
        mContext.startActivity(intent);}
    }
}