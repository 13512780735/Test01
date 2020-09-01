package com.feemoo.adapter;

import android.content.Context;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feemoo.R;
import com.feemoo.utils.DensityUtil;
import com.feemoo.widght.BorderRelativeLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class JXDetailsImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public JXDetailsImgAdapter(int layoutResId, @Nullable List<String> data) {
        super(R.layout.jxdetails_items_img, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        RoundedImageView proImg = helper.getView(R.id.projectImg);
        BorderRelativeLayout bro = helper.getView(R.id.brLayout);
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bro.getLayoutParams();
        int width1 = width - 40;
        params.width = width1;
        params.height = width1 / 2;
        bro.setLayoutParams(params);
        Glide.with(mContext).load(item.toString()).into(proImg);
    }
}
