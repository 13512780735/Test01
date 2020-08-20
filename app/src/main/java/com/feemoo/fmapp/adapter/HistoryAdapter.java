package com.feemoo.fmapp.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.MultipleItemRvAdapter;
import com.feemoo.fmapp.adapter.provider.history.FilesItemPrivider;
import com.feemoo.fmapp.network.model.history.FilesModel;
import com.feemoo.fmapp.network.model.history.HistoryModel;

import java.util.List;

public class HistoryAdapter extends MultipleItemRvAdapter<HistoryModel, BaseViewHolder> {
    public static final int TYPE_FILES = 0;


    public HistoryAdapter(List<HistoryModel> data) {
        super(data);
        finishInitialize();
    }


    @Override
    protected int getViewType(HistoryModel historyModel) {
        //返回对应的viewType
        if (historyModel instanceof FilesModel) {
            return TYPE_FILES;
        }
        return 0;
    }

    @Override
    public void registerItemProvider() {
        //注册相关的条目provider
        mProviderDelegate.registerProvider(new FilesItemPrivider());
    }
}
