package com.fmapp.test01.activity.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import com.fmapp.test01.R;
import com.fmapp.test01.fragment.main.CloudFragment;
import com.fmapp.test01.fragment.main.MyFragment;
import com.fmapp.test01.fragment.main.SelectFragment;
import com.fmapp.test01.utils.AppManager;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.fmapp.test01.utils.StatusBarUtil;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_radio)
    RadioGroup mRadioGroup;

    SelectFragment selectFragment;
    CloudFragment cloudFragment;
    MyFragment myFragment;
    FragmentTransaction mTransaction;
    FragmentManager mSupportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int color = getResources().getColor(R.color.gray_background_color);
        StatusBarUtil.setColor(this, color, 0);
        StatusBarUtil.setLightMode(this);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initFragment();
    }

    private void initFragment() {
        Log.d("token", SharedPreferencesUtils.getString(this, "token"));
        mSupportFragmentManager = getSupportFragmentManager();
        mTransaction = mSupportFragmentManager.beginTransaction();
        selectFragment = SelectFragment.newInstance();
        cloudFragment = CloudFragment.newInstance();
        myFragment = MyFragment.newInstance();
        //初始化展示HomePageFragment
        mTransaction.add(R.id.main_frag, selectFragment)
                .add(R.id.main_frag, cloudFragment)
                .add(R.id.main_frag, myFragment);
        mTransaction.show(selectFragment).hide(cloudFragment).hide(myFragment);
        mTransaction.commit();
        mRadioGroup.check(mRadioGroup.getChildAt(0).getId());
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                FragmentTransaction transaction2 = mSupportFragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.radioButton:
                        transaction2.show(selectFragment).hide(cloudFragment).hide(myFragment).commit();
                        break;
                    case R.id.radioButton2:
                        transaction2.show(cloudFragment).hide(selectFragment).hide(myFragment).commit();
                        break;
                    case R.id.radioButton3:
                        transaction2.show(myFragment).hide(cloudFragment).hide(selectFragment).commit();
                        break;
                }
            }
        });
    }


}
