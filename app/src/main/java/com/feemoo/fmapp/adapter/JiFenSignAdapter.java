package com.feemoo.fmapp.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.network.model.JiFenSignModel;
import com.feemoo.fmapp.widght.BorderTextView;

import java.util.List;

public class JiFenSignAdapter extends BaseQuickAdapter<JiFenSignModel, BaseViewHolder> {
    public JiFenSignAdapter(int layoutResId, @Nullable List<JiFenSignModel> data) {
        super(R.layout.jifen_sign_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JiFenSignModel item) {
        BorderTextView tvNum = helper.getView(R.id.tvNum);
        helper.setText(R.id.tvDay, item.getDay());
        tvNum.setText("+"+item.getNum());
        if (item.isCheck()) {
            tvNum.setContentColorResource01(mContext.getResources().getColor(R.color.jifen_qiandao01));
            tvNum.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            tvNum.setContentColorResource01(mContext.getResources().getColor(R.color.gray_background_color));
            tvNum.setTextColor(mContext.getResources().getColor(R.color.text_color));
        }
    }
}
