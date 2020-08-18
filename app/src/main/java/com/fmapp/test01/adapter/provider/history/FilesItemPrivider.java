package com.fmapp.test01.adapter.provider.history;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chaychan.adapter.BaseItemProvider;
import com.fmapp.test01.R;
import com.fmapp.test01.adapter.HistoryAdapter;
import com.fmapp.test01.fragment.cloud.history.showHistoryBottomDialog;
import com.fmapp.test01.network.model.history.FilesModel;
import com.fmapp.test01.utils.Utils;

import static com.fmapp.test01.utils.com.GetHeaderImgById;

public class FilesItemPrivider extends BaseItemProvider<FilesModel, BaseViewHolder> {
    @Override
    public int viewType() {
        return HistoryAdapter.TYPE_FILES;
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
        showHistoryBottomDialog dialog = new showHistoryBottomDialog();
        dialog.BottomDialog(mContext, data, "0", position);}
    }
}
