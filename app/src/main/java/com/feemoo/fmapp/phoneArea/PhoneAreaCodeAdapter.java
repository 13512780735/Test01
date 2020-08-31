package com.feemoo.fmapp.phoneArea;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feemoo.fmapp.R;

import java.util.List;

/**
 * 创建：yiang
 * <p>
 * 描述：
 */
public class PhoneAreaCodeAdapter extends BaseQuickAdapter<AreaCodeModel, BaseViewHolder> {

    public PhoneAreaCodeAdapter(int layoutResId, @Nullable List<AreaCodeModel> data) {
        super(R.layout.item_area_code, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AreaCodeModel item) {
        helper.setText(R.id.tvArea, item.getName());
        helper.setText(R.id.tvCode, item.getTel());
    }

}
