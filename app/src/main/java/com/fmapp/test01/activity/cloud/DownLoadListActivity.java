package com.fmapp.test01.activity.cloud;


import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.fmapp.test01.R;
import com.fmapp.test01.adapter.VideoListAdapter;
import com.fmapp.test01.base.BaseActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.util.List;

import butterknife.BindView;
import jaygoo.library.m3u8downloader.M3U8Downloader;
import jaygoo.library.m3u8downloader.M3U8DownloaderConfig;
import jaygoo.library.m3u8downloader.OnM3U8DownloadListener;
import jaygoo.library.m3u8downloader.bean.M3U8Task;

public class DownLoadListActivity extends BaseActivity {
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @BindView(R.id.tv_back)
    ImageView mBack;
    private String link, ext;
    M3U8Task[] taskList = new M3U8Task[5];
    private String dirPath;
    private VideoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_list);
        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("link");
        ext = bundle.getString("ext");
        requestAppPermissions();
        initUI();
    }

    private void requestAppPermissions() {
        Dexter.withActivity(this)
                .withPermissions(PERMISSIONS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            initView();
                            Toast.makeText(getApplicationContext(), "权限获取成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "权限获取失败", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    }
                })
                .check();
    }

    private void initView() {
        dirPath = StorageUtils.getCacheDirectory(this).getPath() + "/m3u8Downloader";
        M3U8DownloaderConfig
                .build(getApplicationContext())
                .setSaveDir(dirPath)
                .setDebugMode(true)
        ;
        M3U8Downloader.getInstance().setOnM3U8DownloadListener(onM3U8DownloadListener);
        initData();
        adapter = new VideoListAdapter(this, R.layout.list_item, taskList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = taskList[position].getUrl();
                if (M3U8Downloader.getInstance().checkM3U8IsExist(url)) {
                    Toast.makeText(getApplicationContext(), "本地文件已下载，正在播放中！！！", Toast.LENGTH_SHORT).show();
                } else {
                    M3U8Downloader.getInstance().download(url);
                }
            }
        });

    }

    private void initData() {

        Log.d("link", link);
       // taskList[0] = new M3U8Task(link);
        M3U8Task bean0 = new M3U8Task("http://hls.ciguang.tv/hdtv/video.m3u8");
        M3U8Task bean1 = new M3U8Task("http://pl-ali.youku.com/playlist/m3u8?ts=1524205957&keyframe=0&m3u8Md5=a85842b9ca4e77db4aa57c314c8e61c7&t1=200&pid=1133275aa6ac0891&vid=XMzU1MDY0NjEyMA==&type=flv&oip=1779113856&sid=0524205957937209643a0&token=2124&did=ae8263a35f7eaca76f68bb61436e6dac&ev=1&ctype=20&ep=YlUi3d%2BWQ%2F5shnijRhmbvlc%2FYJ8QmCsaCWAJ1RRpNbA%3D&ymovie=1");
        M3U8Task bean2 = new M3U8Task("https://media6.smartstudy.com/ae/07/3997/2/dest.m3u8");
        M3U8Task bean3 = new M3U8Task("https://www3.laqddc.com/hls/2018/04/07/BQ2cqpyZ/playlist.m3u8");
        M3U8Task bean4 = new M3U8Task("http://hcjs2ra2rytd8v8np1q.exp.bcevod.com/mda-hegtjx8n5e8jt9zv/mda-hegtjx8n5e8jt9zv.m3u8");
        taskList[0] = bean0;
        taskList[1] = bean1;
        taskList[2] = bean2;
        taskList[3] = bean3;
        taskList[4] = bean4;
    }

    private OnM3U8DownloadListener onM3U8DownloadListener = new OnM3U8DownloadListener() {

        @Override
        public void onDownloadItem(M3U8Task task, long itemFileSize, int totalTs, int curTs) {
            super.onDownloadItem(task, itemFileSize, totalTs, curTs);
        }

        @Override
        public void onDownloadSuccess(M3U8Task task) {
            super.onDownloadSuccess(task);
            adapter.notifyChanged(taskList, task);
        }

        @Override
        public void onDownloadPending(M3U8Task task) {
            super.onDownloadPending(task);
            notifyChanged(task);
        }

        @Override
        public void onDownloadPause(M3U8Task task) {
            super.onDownloadPause(task);
            notifyChanged(task);
        }

        @Override
        public void onDownloadProgress(final M3U8Task task) {
            super.onDownloadProgress(task);
            notifyChanged(task);
        }

        @Override
        public void onDownloadPrepare(final M3U8Task task) {
            super.onDownloadPrepare(task);
            notifyChanged(task);

        }

        @Override
        public void onDownloadError(final M3U8Task task, Throwable errorMsg) {
            super.onDownloadError(task, errorMsg);
            notifyChanged(task);
        }

    };

    private void notifyChanged(final M3U8Task task) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyChanged(taskList, task);
            }
        });

    }

    private void initUI() {
        setBackView();
        mBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        setTitle("下载列表");
    }
}
