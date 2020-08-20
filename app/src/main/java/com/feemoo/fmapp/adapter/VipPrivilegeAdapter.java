package com.feemoo.fmapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.network.model.VipPrivilegeModel;

import java.util.List;
import java.util.Map;

public class VipPrivilegeAdapter extends BaseQuickAdapter<VipPrivilegeModel, BaseViewHolder> {
    public VipPrivilegeAdapter(int layoutResId, @Nullable List<VipPrivilegeModel> data) {
        super(R.layout.layout_privilege_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipPrivilegeModel item) {
        Glide.with(mContext).load(item.getImg()).into((ImageView) helper.getView(R.id.ivAvatar));
        helper.setText(R.id.tvName,item.getName());
    }
//    private Context context;
//    private List<Map<String, Object>> listitem;
//
//    public VipPrivilegeAdapter(Context context, List<Map<String, Object>> listitem) {
//        this.context = context;
//        this.listitem = listitem;
//    }
//
//    @Override
//    public int getCount() {
//        return listitem.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return listitem.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.layout_privilege_item, null);
//        }
//
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivAvatar);
//        TextView textView = (TextView) convertView.findViewById(R.id.tvName);
//
//        Map<String, Object> map = listitem.get(position);
//        imageView.setImageResource((Integer) map.get("image"));
//        textView.setText(map.get("title") + "");
//        return convertView;
//    }
}
