package com.fmapp.test01.adapter.provider.workstation;

import android.content.Intent;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.fmapp.test01.R;
import com.fmapp.test01.activity.file.cloud.FilePreviewActivity;
import com.fmapp.test01.activity.file.work.workFilePreviewActivity;
import com.fmapp.test01.adapter.WorkStationAdapter;
import com.fmapp.test01.network.model.workStation.FolderModel;
import com.fmapp.test01.utils.Utils;


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