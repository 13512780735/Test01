package com.fmapp.test01.fragment.main;


import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.fmapp.test01.R;
import com.fmapp.test01.activity.cloud.DownLoadListActivity;
import com.fmapp.test01.base.BaseFragment;
import com.fmapp.test01.fragment.cloud.cloud2.Cloud2Fragment;
import com.fmapp.test01.fragment.cloud.history.DownloadHistoryFragment;
import com.fmapp.test01.fragment.cloud.star.StarFragment;
import com.fmapp.test01.fragment.cloud.work.WorkStationFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloudFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTitleName;
    private ImageView mHeader01;
    private ImageView mHeader02;
    private ImageView mHeader03;
    private RadioGroup mRadioGroup;

    private boolean isGetData = false;
    private Cloud2Fragment cloud2Fragment;
    private WorkStationFragment workStationFragment;
    private StarFragment starFragment;
    private DownloadHistoryFragment downloadHistoryFragment;
    private List<Fragment> list;
    private RadioButton mRb01, mRb02, mRb03, mRb04;
    private String titleName;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isGetData) {
            isGetData = true;
            //   这里可以做网络请求或者需要的数据刷新操作
//            GetData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    public static CloudFragment newInstance() {
        return new CloudFragment();
    }

    @Override
    protected void initView(View view) {
        mTitleName = findView(R.id.tv_title);
        mHeader01 = findView(R.id.iv_main_header01);
        mHeader02 = findView(R.id.iv_main_header02);
        mHeader03 = findView(R.id.iv_main_header03);
        mTitleName.setText("飞猫云");
        mHeader01.setImageDrawable(getResources().getDrawable(R.mipmap.icon_search));
        mHeader02.setImageDrawable(getResources().getDrawable(R.mipmap.icon_add));
        mHeader03.setImageDrawable(getResources().getDrawable(R.mipmap.icon_down01));
        mRadioGroup = findView(R.id.mRadioGroup);
        mRb01 = findView(R.id.mRb01);
        mRb02 = findView(R.id.mRb02);
        mRb03 = findView(R.id.mRb03);
        mRb04 = findView(R.id.mRb04);
        cloud2Fragment = new Cloud2Fragment();
        workStationFragment = new WorkStationFragment();
        starFragment = new StarFragment();
        downloadHistoryFragment = new DownloadHistoryFragment();

        list = new ArrayList<>();
        list.add(cloud2Fragment);
        list.add(workStationFragment);
        list.add(starFragment);
        list.add(downloadHistoryFragment);
        mRadioGroup.check(R.id.mRb01);
        titleName = "云空间";
        addFragment(cloud2Fragment);


        mRb01.setOnClickListener(this);
        mRb02.setOnClickListener(this);
        mRb03.setOnClickListener(this);
        mRb04.setOnClickListener(this);

        mHeader01.setOnClickListener(this);
        mHeader02.setOnClickListener(this);
        mHeader03.setOnClickListener(this);
    }


    //向Activity中添加Fragment的方法
    public void addFragment(Fragment fragment) {

        //获得Fragment管理器
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //使用管理器开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //使用事务替换Fragment容器中Fragment对象
        fragmentTransaction.replace(R.id.main_frag1, fragment);
        //提交事务，否则事务不生效
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRb01:
                addFragment(cloud2Fragment);
                titleName = "云空间";
                break;
            case R.id.mRb02:
                addFragment(workStationFragment);
                titleName = "操作台";
                break;
            case R.id.mRb03:
                addFragment(starFragment);
                titleName = "星标";
                break;
            case R.id.mRb04:
                addFragment(downloadHistoryFragment);
                titleName = "下载历史";
                break;
            case R.id.iv_main_header01://搜索
                break;
            case R.id.iv_main_header02://扫一扫

                break;
            case R.id.iv_main_header03://下载
                toActivity(DownLoadListActivity.class);
                break;
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_cloud;
    }


    @Override
    protected void initData(Context mContext) {

    }
}
