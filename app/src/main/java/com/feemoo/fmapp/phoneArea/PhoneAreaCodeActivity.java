package com.feemoo.fmapp.phoneArea;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PhoneAreaCodeActivity extends BaseActivity {
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    private List<AreaCodeModel> datalist = new ArrayList<>();
    private PhoneAreaCodeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_area_code);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        initUI();
        getData();
    }

    private void getData() {
        String json = Utils.readAssetsTxt(this, "phoneAreaCode");
        datalist = Utils.jsonToList(json);
        mAdapter.setNewData(datalist);
        mAdapter.notifyDataSetChanged();
    }

    private void initUI() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new PhoneAreaCodeAdapter(R.layout.item_area_code, datalist);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String tel = datalist.get(position).getTel();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("tel", tel);
                intent.putExtras(bundle);
                setResult(1, intent);
                finish();

            }
        });
    }
}
