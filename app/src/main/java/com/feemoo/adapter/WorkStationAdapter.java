package com.feemoo.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.MultipleItemRvAdapter;
import com.feemoo.adapter.provider.workstation.FilesItemPrivider;
import com.feemoo.adapter.provider.workstation.FolderItemPrivider;
import com.feemoo.adapter.provider.workstation.SpaceItemPrivider;
import com.feemoo.network.model.workStation.FilesModel;
import com.feemoo.network.model.workStation.FolderModel;
import com.feemoo.network.model.workStation.SpaceModel;
import com.feemoo.network.model.workStation.workStationModel;

import java.util.List;

public class WorkStationAdapter extends MultipleItemRvAdapter<workStationModel, BaseViewHolder> {
    public static final int TYPE_FOLDER = 0;
    public static final int TYPE_FILES = 1;
    public static final int TYPE_SPACE = 2;

    public WorkStationAdapter(List<workStationModel> data) {
        super(data);
        finishInitialize();
    }


    @Override
    protected int getViewType(workStationModel workStationModel01) {
        //返回对应的viewType
        if (workStationModel01 instanceof FolderModel) {
            return TYPE_FOLDER;
        } else if (workStationModel01 instanceof FilesModel) {
            return TYPE_FILES;
        } else if (workStationModel01 instanceof SpaceModel) {
            return TYPE_SPACE;
        }
        return 0;
    }

    @Override
    public void registerItemProvider() {
        //注册相关的条目provider
        mProviderDelegate.registerProvider(new FilesItemPrivider());
        mProviderDelegate.registerProvider(new FolderItemPrivider());
        mProviderDelegate.registerProvider(new SpaceItemPrivider());
    }
}
