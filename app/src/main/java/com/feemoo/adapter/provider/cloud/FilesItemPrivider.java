package com.feemoo.adapter.provider.cloud;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.feemoo.R;
import com.feemoo.adapter.CloudAdapter;
import com.feemoo.fragment.cloud.cloud2.showCloudBottomDialog;
import com.feemoo.network.model.cloud.FilesModel;
import com.feemoo.utils.Utils;
import com.feemoo.widght.BorderTextView;

import static com.feemoo.utils.com.GetHeaderImgById;

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
        BorderTextView mTvShare = baseViewHolder.getView(R.id.tvIsShare);

        String isShare = filesModel.getIsshare();
        if ("1".equals(isShare)) {
            mTvShare.setVisibility(View.VISIBLE);
            mTvShare.setContentColorResource01(mContext.getResources().getColor(R.color.yellow_button_bg_pressed_color));
            mTvShare.setText("公");
        } else {
            mTvShare.setVisibility(View.VISIBLE);
            mTvShare.setContentColorResource01(mContext.getResources().getColor(R.color.blue_button_bg_pressed_color));
            mTvShare.setText("私");
        }


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
