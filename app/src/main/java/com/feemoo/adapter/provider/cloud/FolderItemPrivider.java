package com.feemoo.adapter.provider.cloud;

import android.content.Intent;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.feemoo.R;
import com.feemoo.activity.file.cloud.FilePreviewActivity;
import com.feemoo.adapter.CloudAdapter;
import com.feemoo.network.model.cloud.FolderModel;
import com.feemoo.utils.Utils;

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
        if (Utils.isFastClick()) {
        Intent intent=new Intent(mContext, FilePreviewActivity.class);
        intent.putExtra("name",folderModel.getName());
        intent.putExtra("id",folderModel.getId());
        mContext.startActivity(intent);}

    }

    @Override
    public boolean onLongClick(BaseViewHolder baseViewHolder, FolderModel folderModel, int position) {
        return false;
    }
}
