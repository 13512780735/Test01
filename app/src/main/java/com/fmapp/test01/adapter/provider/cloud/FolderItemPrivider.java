package com.fmapp.test01.adapter.provider.cloud;

import android.content.Intent;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.fmapp.test01.R;
import com.fmapp.test01.activity.file.cloud.FilePreviewActivity;
import com.fmapp.test01.adapter.CloudAdapter;
import com.fmapp.test01.network.model.cloud.FolderModel;

public class FolderItemPrivider extends BaseItemProvider<FolderModel, BaseViewHolder> {


    @Override
    public int viewType() {
        return CloudAdapter.TYPE_FOLDER;
    }

    @Override
    public int layout() {
        return R.layout.cloud_folder_item;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, FolderModel folderModel, int position) {
        baseViewHolder.setText(R.id.tvName, folderModel.getName());

        baseViewHolder.setText(R.id.tvContent, folderModel.getIntime());
    }

    @Override
    public void onClick(BaseViewHolder baseViewHolder, FolderModel folderModel, int position) {
        Intent intent=new Intent(mContext, FilePreviewActivity.class);
        intent.putExtra("name",folderModel.getName());
        intent.putExtra("id",folderModel.getId());
        mContext.startActivity(intent);

    }

    @Override
    public boolean onLongClick(BaseViewHolder baseViewHolder, FolderModel folderModel, int position) {
        return false;
    }
}
