package com.fmapp.test01.fragment.main;



import androidx.fragment.app.Fragment;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmapp.test01.R;
import com.fmapp.test01.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFragment extends BaseFragment {

    private TextView mTitleName;
    private ImageView mHeader01;
    private ImageView mHeader02;
    private ImageView mHeader03;

    public static SelectFragment newInstance() {
        return new SelectFragment();
    }


    @Override
    protected int setContentView() {
        return R.layout.fragment_select;
    }

    @Override
    protected void lazyLoad() {


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SelectFragment","执行类");
        initUI();
    }

    private void initUI() {
        mTitleName = findView(R.id.tv_title);
        mHeader01 = findView(R.id.iv_main_header01);
        mHeader02 = findView(R.id.iv_main_header02);
        mHeader03 = findView(R.id.iv_main_header03);
        mTitleName.setText("鲸选下载");
        mHeader01.setImageDrawable(getResources().getDrawable(R.mipmap.icon_search));
        mHeader02.setImageDrawable(getResources().getDrawable(R.mipmap.icon_saoyisao));
        mHeader03.setImageDrawable(getResources().getDrawable(R.mipmap.icon_down01));
        mHeader01.setOnClickListener(view -> showToast("点击了"));
    }

}
