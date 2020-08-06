package com.fmapp.test01.activity.cloud;


import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arialyy.annotations.Download;
import com.arialyy.annotations.DownloadGroup;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.AbsEntity;
import com.arialyy.aria.core.task.DownloadGroupTask;
import com.arialyy.aria.core.task.DownloadTask;
import com.arialyy.aria.util.ALog;
import com.fmapp.test01.R;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.download.DownloadAdapter;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DownLoadListActivity extends BaseActivity {
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private RecyclerView mRecycleView;

    @BindView(R.id.tv_back)
    ImageView mBack;
   // private String link, ext, name, size, id;

    private DownloadAdapter mAdapter;
    private List<AbsEntity> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_list);
//        Bundle bundle = getIntent().getExtras();
//        id = bundle.getString("id");
//        link = bundle.getString("link");
//        ext = bundle.getString("ext");
//        name = bundle.getString("name");
//        size = bundle.getString("size");
//        Log.d("id:->", id);
//        Log.d("link:->", link);
//        Log.d("ext:->", ext);
//        Log.d("name:->", name);
        Aria.download(this).register();
        initUI();

    }


    private void initUI() {
        setBackView();
        mBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        setTitle("下载列表");
        mRecycleView = findView(R.id.mRecycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        List<AbsEntity> temps = Aria.download(this).getTotalTaskList();
        if (temps != null && !temps.isEmpty()) {

            for (AbsEntity temp : temps) {
            }
            mData.addAll(temps);
        }
        mAdapter = new DownloadAdapter(this, mData);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Download.onPre
    void onPre(DownloadTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @Download.onWait
    void onWait(DownloadTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @Download.onTaskStart
    void taskStart(DownloadTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @Download.onTaskResume
    void taskResume(DownloadTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @Download.onTaskStop
    void taskStop(DownloadTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @Download.onTaskCancel
    void taskCancel(DownloadTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @Download.onTaskFail
    void taskFail(DownloadTask task) {
        if (task == null || task.getEntity() == null){
            return;
        }
        mAdapter.updateState(task.getEntity());
    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @Download.onTaskRunning void taskRunning(DownloadTask task) {
        mAdapter.setProgress(task.getEntity());
    }

    //////////////////////////////////// 下面为任务组的处理 /////////////////////////////////////////

    @DownloadGroup.onPre
    void onGroupPre(DownloadGroupTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @DownloadGroup.onTaskStart
    void groupTaskStart(DownloadGroupTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @DownloadGroup.onWait
    void groupTaskWait(DownloadGroupTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @DownloadGroup.onTaskResume
    void groupTaskResume(DownloadGroupTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @DownloadGroup.onTaskStop
    void groupTaskStop(DownloadGroupTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @DownloadGroup.onTaskCancel
    void groupTaskCancel(DownloadGroupTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @DownloadGroup.onTaskFail
    void groupTaskFail(DownloadGroupTask task) {
        if (task != null) {
            mAdapter.updateState(task.getEntity());
        }
    }

    @DownloadGroup.onTaskComplete
    void groupTaskComplete(DownloadGroupTask task) {
        mAdapter.updateState(task.getEntity());
    }

    @DownloadGroup.onTaskRunning()
    void groupTaskRunning(DownloadGroupTask task) {
        mAdapter.setProgress(task.getEntity());
    }
}
