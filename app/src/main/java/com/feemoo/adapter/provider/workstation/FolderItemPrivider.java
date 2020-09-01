package com.feemoo.adapter.provider.workstation;

import android.content.Intent;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.feemoo.R;
import com.feemoo.activity.file.work.workFilePreviewActivity;
import com.feemoo.adapter.WorkStationAdapter;
import com.feemoo.network.model.workStation.FolderModel;
import com.feemoo.utils.Utils;


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