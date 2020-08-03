package com.fmapp.test01.fragment.main;


import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.fmapp.test01.R;
import com.fmapp.test01.base.BaseFragment;
import com.fmapp.test01.fragment.cloud.Cloud2Fragment;
import com.fmapp.test01.fragment.cloud.DownloadHistoryFragment;
import com.fmapp.test01.fragment.cloud.StarFragment;
import com.fmapp.test01.fragment.cloud.WorkStationFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloudFragment extends BaseFragment {
    private TextView mTitleName;
    private ImageView mHeader01;
    private ImageView mHeader02;
    private ImageView mHeader03;
    private RadioGroup mRadioGroup;

    private boolean isGetData = false;
    private FragmentManager mSupportFragmentManager;
    private FragmentTransaction mTran1saction;
    private Cloud2Fragment cloud2Fragment;
    private WorkStationFragment workStationFragment;
    private StarFragment starFragment;
    private DownloadHistoryFragment downloadHistoryFragment;

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
    protected int setContentView() {
        return R.layout.fragment_cloud;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
    }

    private void initUI() {
        mTitleName = findView(R.id.tv_title);
        mHeader01 = findView(R.id.iv_main_header01);
        mHeader02 = findView(R.id.iv_main_header02);
        mHeader03 = findView(R.id.iv_main_header03);
        mTitleName.setText("飞猫云");
        mHeader01.setImageDrawable(getResources().getDrawable(R.mipmap.icon_search));
        mHeader02.setImageDrawable(getResources().getDrawable(R.mipmap.icon_add));
        mHeader03.setImageDrawable(getResources().getDrawable(R.mipmap.icon_down01));
        mSupportFragmentManager = getActivity().getSupportFragmentManager();
        mTran1saction = mSupportFragmentManager.beginTransaction();
        cloud2Fragment = Cloud2Fragment.newInstance();
        workStationFragment = WorkStationFragment.newInstance();
        starFragment = StarFragment.newInstance();
        downloadHistoryFragment = DownloadHistoryFragment.newInstance();
        //初始化展示HomePageFragment
        mTran1saction.add(R.id.main_frag1, cloud2Fragment)
                .add(R.id.main_frag1, workStationFragment)
                .add(R.id.main_frag1, starFragment)
                .add(R.id.main_frag1, downloadHistoryFragment);
        mTran1saction.show(cloud2Fragment).hide(workStationFragment).hide(starFragment).hide(downloadHistoryFragment);
        mTran1saction.commit();
        mRadioGroup = findView(R.id.mRadioGroup);
        mRadioGroup.check(mRadioGroup.getChildAt(0).getId());
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                FragmentTransaction transaction2 = mSupportFragmentManager.beginTransaction();
                switch (i){
                    case R.id.mRb01:
                        transaction2.show(cloud2Fragment).hide(workStationFragment).hide(starFragment).hide(downloadHistoryFragment).commit();
                        break;
                    case R.id.mRb02:
                        transaction2.show(workStationFragment).hide(cloud2Fragment).hide(starFragment).hide(downloadHistoryFragment).commit();
                        break;
                    case R.id.mRb03:
                        transaction2.show(starFragment).hide(cloud2Fragment).hide(workStationFragment).hide(downloadHistoryFragment).commit();
                        break;
                    case R.id.mRb04:
                        transaction2.show(downloadHistoryFragment).hide(cloud2Fragment).hide(workStationFragment).hide(starFragment).commit();
                        break;
                }
            }
        });
    }



}
