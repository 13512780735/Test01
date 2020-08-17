package com.fmapp.test01.fragment.select;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.fmapp.test01.R;
import com.fmapp.test01.activity.cloud.DownLoadActivity;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.JXNavModel;
import com.fmapp.test01.network.model.cloud.FilesModel;
import com.fmapp.test01.network.model.select.JXHomeModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.CustomDialog;
import com.fmapp.test01.utils.LoaddingDialog;
import com.fmapp.test01.utils.SharedPreferencesUtils;
import com.fmapp.test01.utils.ToastUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Subscriber;

public class showSelectBottomDialog {
    private View view;
    private LoaddingDialog loaddingDialog;
    private Dialog dialog;
    private String scid = String.valueOf(0);
    private String scid2 = String.valueOf(0);

    //type 0 文件类 1，在线压缩包
    public void BottomDialog(Context context, List<JXNavModel> jxNavModels, int position) {
        loaddingDialog = new LoaddingDialog(context);
        //1、使用Dialog、设置style
        dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        view = View.inflate(context, R.layout.select_dialog, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        initData(context, jxNavModels, position);
    }

    List<String> mTag01 = new ArrayList<>();
    List<String> mTag02 = new ArrayList<>();
    List<JXNavModel.ChildBeanX.ChildBean> childBeanList = new ArrayList<>();

    private void initData(Context context, List<JXNavModel> jxNavModels, int position) {
        TextView tvName01 = dialog.findViewById(R.id.tvName01);
        TextView tvName02 = dialog.findViewById(R.id.tvName02);
        TagFlowLayout mTagFlowLayout01 = dialog.findViewById(R.id.id_flowlayout01);
        TagFlowLayout mTagFlowLayout02 = dialog.findViewById(R.id.id_flowlayout02);

        tvName01.setText(jxNavModels.get(position).getName());
        tvName02.setVisibility(View.GONE);
        mTagFlowLayout02.setVisibility(View.GONE);
        for (int i = 0; i < jxNavModels.get(position).getChild().size(); i++) {
            mTag01.add(jxNavModels.get(position).getChild().get(i).getName());
        }
        TagAdapter<String> adapter = new TagAdapter<String>(mTag01) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false);
                view.setText(o);
                return view;
            }
        };
        mTagFlowLayout01.setAdapter(adapter);
        mTagFlowLayout01.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int j, FlowLayout parent) {
                //view.setVisibility(View.GONE);
                scid = jxNavModels.get(position).getChild().get(j).getId();
                if (mTag02.size() > 0) {
                    mTag02.clear();
                    mTagFlowLayout02.setVisibility(View.GONE);
                    tvName02.setVisibility(View.GONE);
                } else {
                    mTagFlowLayout02.setVisibility(View.VISIBLE);
                    tvName02.setVisibility(View.VISIBLE);
                    tvName02.setText(jxNavModels.get(position).getChild().get(j).getName());
                    childBeanList = jxNavModels.get(position).getChild().get(j).getChild();
                    if (childBeanList != null && childBeanList.size() > 0) {
                        for (int i = 0; i < jxNavModels.get(position).getChild().get(j).getChild().size(); i++) {
                            mTag02.add(jxNavModels.get(position).getChild().get(j).getChild().get(i).getName());
                        }
                        TagAdapter<String> adapter = new TagAdapter<String>(mTag02) {
                            @Override
                            public View getView(FlowLayout parent, int position, String o) {
                                TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false);
                                view.setText(o);
                                return view;
                            }
                        };
                        mTagFlowLayout02.setAdapter(adapter);
                        mTagFlowLayout02.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                            @Override
                            public boolean onTagClick(View view, int z, FlowLayout parent) {
                                scid2 = jxNavModels.get(position).getChild().get(j).getChild().get(z).getId();
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.select");
                                intent.putExtra("scid", scid);
                                intent.putExtra("scid2", scid2);
                                context.sendBroadcast(intent);
                                dialog.dismiss();
                                return true;
                            }
                        });

                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.select");
                        intent.putExtra("scid", scid);
                        intent.putExtra("scid2", scid2);
                        context.sendBroadcast(intent);
                        dialog.dismiss();
                    }
                }
                return true;
            }
        });
    }


    public void showToast(Context context, String msg) {
        ToastUtil toastUtil = new ToastUtil(context, R.layout.toast_center, msg);
        toastUtil.show();
    }


}
