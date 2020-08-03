/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 下午2:46
 * ********************************************************
 */
package com.fmapp.test01.widght.RecycleViewUtils;


import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fmapp.test01.R;
import com.zcolin.gui.zrecyclerview.BaseRecyclerAdapter;
import com.zcolin.gui.zrecyclerview.swipemenu.SwipeMenuLayout;


public class SwipeMenuRecyclerAdapter extends BaseRecyclerAdapter<String> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.view_swipemenu;
    }

    @Override
    public void setUpData(CommonHolder holder, int position, int viewType, String data) {
        final SwipeMenuLayout superSwipeMenuLayout = (SwipeMenuLayout) holder.itemView;
        superSwipeMenuLayout.setSwipeEnable(true);//设置是否可以侧滑
        TextView tvOpen = getView(holder, R.id.btOpen);
        TextView tvDel = getView(holder, R.id.btDelete);
        ImageView iv = getView(holder, R.id.image_iv);
        TextView tvName = getView(holder, R.id.name_tv);

        tvName.setText(String.format("第%d条数据", position));

        tvOpen.setOnClickListener(v -> Toast.makeText(superSwipeMenuLayout.getContext(), "Open", Toast.LENGTH_SHORT).show());
        tvDel.setOnClickListener(v -> Toast.makeText(superSwipeMenuLayout.getContext(), "Delete", Toast.LENGTH_SHORT).show());

        iv.setOnClickListener(v -> {
            if (superSwipeMenuLayout.isOpen()) {
                superSwipeMenuLayout.closeMenu();
            } else {
                superSwipeMenuLayout.openMenu();
            }
        });
    }
}