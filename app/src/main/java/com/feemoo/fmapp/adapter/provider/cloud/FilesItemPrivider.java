package com.feemoo.fmapp.adapter.provider.cloud;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.adapter.CloudAdapter;
import com.feemoo.fmapp.fragment.cloud.cloud2.showCloudBottomDialog;
import com.feemoo.fmapp.network.model.cloud.FilesModel;
import com.feemoo.fmapp.utils.Utils;

import static com.feemoo.fmapp.utils.com.GetHeaderImgById;

public class FilesItemPrivider extends BaseItemProvider<FilesModel, BaseViewHolder> {
    @Override
    public int viewType() {
        return CloudAdapter.TYPE_FILES;
    }

    @Override
    public int layout() {
        return R.layout.cloud_files_item;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, FilesModel filesModel, int position) {
        ImageView imageView = baseViewHolder.getView(R.id.ivPic);
        imageView.setImageResource(GetHeaderImgById(filesModel.getExt()));
        baseViewHolder.setText(R.id.tvName, filesModel.getName());
        baseViewHolder.setText(R.id.tvContent, filesModel.getSize() + "  " + filesModel.getIntime());

    }

    @Override
    public void onClick(BaseViewHolder helper, FilesModel data, int position) {
        super.onClick(helper, data, position);
        if (Utils.isFastClick()) {
            showCloudBottomDialog dialog = new showCloudBottomDialog();
            dialog.BottomDialog(mContext, data, "0", position);
        }
    }
}
