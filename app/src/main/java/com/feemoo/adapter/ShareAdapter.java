package com.feemoo.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.MultipleItemRvAdapter;
import com.feemoo.adapter.provider.share.FilesItemPrivider;
import com.feemoo.network.model.share.FilesModel;
import com.feemoo.network.model.share.ShareModel;

import java.util.List;

public class ShareAdapter extends MultipleItemRvAdapter<ShareModel, BaseViewHolder> {

    public static final int TYPE_FILES = 0;

    public ShareAdapter(@Nullable List<ShareModel> data) {
        super(data);
        finishInitialize();
    }

    @Override
    protected int getViewType(ShareModel shareModel) {
        //返回对应的viewType
        if (shareModel instanceof FilesModel) {
            return TYPE_FILES;
        }
        return 0;
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new FilesItemPrivider());
    }
}
