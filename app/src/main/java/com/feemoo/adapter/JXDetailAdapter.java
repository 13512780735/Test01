package com.feemoo.adapter;

import android.content.Context;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feemoo.R;
import com.feemoo.network.model.JxDetailsModel;
import com.feemoo.utils.DensityUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class JXDetailAdapter extends BaseQuickAdapter<JxDetailsModel.BsfilesBean, BaseViewHolder> {
    public JXDetailAdapter(int layoutResId, @Nullable List<JxDetailsModel.BsfilesBean> data) {
        super(R.layout.jxdetails_items, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JxDetailsModel.BsfilesBean item) {
        RoundedImageView projectImg = helper.getView(R.id.projectImg);
//        WindowManager wm = (WindowManager) mContext
//                .getSystemService(Context.WINDOW_SERVICE);
//        int width = wm.getDefaultDisplay().getWidth();
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) projectImg.getLayoutParams();
//        params.width = DensityUtil.dip2px(mContext, width - 80);
//        params.height = DensityUtil.dip2px(mContext, width - 80) / 2;
        //projectImg.setLayoutParams(params);
        Glide.with(mContext).load(item.getImg()).into(projectImg);
        helper.setText(R.id.tvName, item.getName());
    }
}
