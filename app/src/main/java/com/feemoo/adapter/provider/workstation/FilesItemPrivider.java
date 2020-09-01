package com.feemoo.adapter.provider.workstation;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.feemoo.R;
import com.feemoo.adapter.WorkStationAdapter;
import com.feemoo.fragment.cloud.work.showworkBottomDialog;
import com.feemoo.network.model.workStation.FilesModel;
import com.feemoo.utils.Utils;

import static com.feemoo.utils.com.GetHeaderImgById;

public class FilesItemPrivider extends BaseItemProvider<FilesModel, BaseViewHolder> {
    @Override
    public int viewType() {
        return WorkStationAdapter.TYPE_FILES;
    }

    @Override
    public int layout() {
        return R.layout.cloud_files_item;
    }

    @Override
    public void convert(BaseViewHolder helper, FilesModel data, int position) {
        ImageView imageView = helper.getView(R.id.ivPic);
        imageView.setImageResource(GetHeaderImgById(data.getExt()));
        helper.setText(R.id.tvName, data.getName());
        helper.setText(R.id.tvContent, data.getSize() + "  " + data.getIntime());
    }

    @Override
    public void onClick(BaseViewHolder helper, FilesModel data, int position) {
        super.onClick(helper, data, position);
        if (Utils.isFastClick()) {
        showworkBottomDialog dialog = new showworkBottomDialog();
        dialog.BottomDialog(mContext, data, "0", position);}
    }
}
