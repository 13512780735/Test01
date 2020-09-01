package com.feemoo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.feemoo.R;
import com.feemoo.network.model.VipModel;
import com.feemoo.widght.BorderRelativeLayout;

import java.util.List;

public class VipLevelAdapter extends RecyclerView.Adapter<VipLevelAdapter.ViewHolder> {


    private int mPosition = 0;

    public void setPosition(int position) {
        mPosition = position;
    }

    private Context context;
    private List<VipModel.SvipsBean> data;

    public VipLevelAdapter(Context context, List<VipModel.SvipsBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_vip_level_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (mPosition == position) {
            holder.rl.setBackground(context.getResources().getDrawable(R.drawable.shape_round_vip_select_bg));
        } else {
            holder.rl.setBackground(context.getResources().getDrawable(R.drawable.shape_round_vip_unselect_bg));
        }
        Glide.with(context).load(data.get(position).getImg()).into(holder.ivImageView);
        holder.mTvDate.setText(data.get(position).getName());
        holder.mTvNewPrice.setText(data.get(position).getPrice());
        holder.mTvOldPrice.setText(data.get(position).getOprice() + "元");
        holder.mTvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImageView;
        TextView mTvNewPrice;
        TextView mTvOldPrice;
        TextView mTvDate;
        RelativeLayout rl;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImageView = itemView.findViewById(R.id.ivImg);
            mTvNewPrice = itemView.findViewById(R.id.tvNewPrice);
            mTvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            mTvDate = itemView.findViewById(R.id.tvDate);
            rl = itemView.findViewById(R.id.rl);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, getLayoutPosition(), data.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    //下面两个方法提供给页面刷新和加载时调用
    public void add(List<VipModel.SvipsBean> addMessageList) {
        //增加数据
        int position = data.size();
        data.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<VipModel.SvipsBean> newList) {
        //刷新数据
        data.removeAll(data);
        data.addAll(newList);
        notifyDataSetChanged();
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, VipModel.SvipsBean svipsBean);
    }
//    public VipLevelAdapter(int layoutResId, @Nullable List<VipModel.SvipsBean> data) {
//        super(R.layout.layout_vip_level_item, data);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, VipModel.SvipsBean item) {

//    }
}
