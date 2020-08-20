package com.feemoo.fmapp.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by liaopenghui on 16/7/28.
 */
public class MyScrollView  extends ScrollView{
    private OnMyScrollChanged onMyScrollChanged;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        onMyScrollChanged.getParams(l, t, oldl, oldt);
    }



    public void setOnScrollChanged(OnMyScrollChanged onMyScrollChanged){
        this.onMyScrollChanged = onMyScrollChanged;
    }


    public interface OnMyScrollChanged{
        void getParams(int l, int t, int oldl, int oldt);
    }
}
