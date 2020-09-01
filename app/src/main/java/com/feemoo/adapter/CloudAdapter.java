package com.feemoo.adapter;


import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.MultipleItemRvAdapter;
import com.feemoo.adapter.provider.cloud.FilesItemPrivider;
import com.feemoo.adapter.provider.cloud.FolderItemPrivider;
import com.feemoo.network.model.cloud.CloudModel;
import com.feemoo.network.model.cloud.FilesModel;
import com.feemoo.network.model.cloud.FolderModel;

import java.util.List;


public class CloudAdapter extends MultipleItemRvAdapter<CloudModel, BaseViewHolder> {
    public static final int TYPE_FOLDER = 0;
    public static final int TYPE_FILES = 1;


    public CloudAdapter(List<CloudModel> data) {
        super(data);
        finishInitialize();
    }


    @Override
    protected int getViewType(CloudModel cloudModel) {
        //返回对应的viewType
        if (cloudModel instanceof FolderModel) {
            return TYPE_FOLDER;
        } else if (cloudModel instanceof FilesModel) {
            return TYPE_FILES;
        }
        return 0;
    }

    @Override
    public void registerItemProvider() {
        //注册相关的条目provider
        mProviderDelegate.registerProvider(new FilesItemPrivider());
        mProviderDelegate.registerProvider(new FolderItemPrivider());
    }
}
