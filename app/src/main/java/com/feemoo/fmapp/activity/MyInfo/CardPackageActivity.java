package com.feemoo.fmapp.activity.MyInfo;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feemoo.fmapp.R;
import com.feemoo.fmapp.adapter.CouponListAdapter;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.fragment.cloud.cloud2.Cloud2Fragment;
import com.feemoo.fmapp.fragment.cloud.share.ShareListFragment;
import com.feemoo.fmapp.fragment.cloud.star.StarFragment;
import com.feemoo.fmapp.fragment.cloud.work.WorkStationFragment;
import com.feemoo.fmapp.fragment.coupon.Coupon01Fragment;
import com.feemoo.fmapp.fragment.coupon.Coupon02Fragment;
import com.feemoo.fmapp.fragment.coupon.Coupon03Fragment;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.CouponModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class CardPackageActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;

    List<Fragment> list = new ArrayList<>();
    private String flag;
    private RadioButton mRb01, mRb02, mRb03;
    private Coupon01Fragment coupon01Fragment;
    private Coupon02Fragment coupon02Fragment;
    private Coupon03Fragment coupon03Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_package);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.gray_background_color).init();
        initUI();
    }


    private void initUI() {
        flag = "0";
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mRb01 = findView(R.id.mRb01);
        mRb02 = findView(R.id.mRb02);
        mRb03 = findView(R.id.mRb03);
        coupon01Fragment = new Coupon01Fragment();
        coupon02Fragment = new Coupon02Fragment();
        coupon03Fragment = new Coupon03Fragment();

        list = new ArrayList<>();
        list.add(coupon01Fragment);
        list.add(coupon02Fragment);
        list.add(coupon03Fragment);
        mRadioGroup.check(R.id.mRb01);
        addFragment(coupon01Fragment);


        mRb01.setOnClickListener(this);
        mRb02.setOnClickListener(this);
        mRb03.setOnClickListener(this);
    }

    //向Activity中添加Fragment的方法
    public void addFragment(Fragment fragment) {

        //获得Fragment管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
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
                addFragment(coupon01Fragment);
                flag = "1";
                break;
            case R.id.mRb02:
                addFragment(coupon02Fragment);
                flag = "2";
                break;
            case R.id.mRb03:
                addFragment(coupon03Fragment);
                flag = "3";
                break;
        }
    }
}
