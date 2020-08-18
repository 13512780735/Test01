package com.fmapp.test01.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fmapp.test01.R;
import com.fmapp.test01.network.model.MoveFoldModel;

import java.util.List;

public class CLoudMoveFileAdpter extends BaseQuickAdapter<MoveFoldModel, BaseViewHolder> {
    public CLoudMoveFileAdpter(int layoutResId, @Nullable List<MoveFoldModel> data) {
        super(R.layout.cloud_folder_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MoveFoldModel item) {
        TextView tvContent = helper.getView(R.id.tvContent);
        ImageView ivMove = helper.getView(R.id.ivMove);
        tvContent.setVisibility(View.GONE);
        ivMove.setVisibility(View.GONE);
        helper.setText(R.id.tvName, item.getName());

    }
}
