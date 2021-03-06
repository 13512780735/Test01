package com.feemoo.activity.cloud;


import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidev.download.DownloadInfo;
import com.androidev.download.DownloadJobListener;
import com.androidev.download.DownloadListener;
import com.androidev.download.DownloadManager;
import com.androidev.download.DownloadTask;
import com.feemoo.R;
import com.feemoo.activity.file.showOnlineDialog;
import com.feemoo.base.BaseActivity;
import com.feemoo.download.NetSpeed;
import com.feemoo.download.NetSpeedTimer;
import com.feemoo.download.NetWorkSpeedUtils;
import com.feemoo.download.util.FileManager;
import com.feemoo.utils.CustomDialog;
import com.feemoo.utils.Utils;
import com.feemoo.widght.CircleProgressBar;
import com.feemoo.widght.SwipeItemLayout;
import com.gyf.immersionbar.ImmersionBar;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static com.androidev.download.DownloadState.STATE_FAILED;
import static com.androidev.download.DownloadState.STATE_FINISHED;
import static com.androidev.download.DownloadState.STATE_PAUSED;
import static com.androidev.download.DownloadState.STATE_PREPARED;
import static com.androidev.download.DownloadState.STATE_RUNNING;
import static com.androidev.download.DownloadState.STATE_WAITING;
import static com.feemoo.utils.com.GetHeaderImgById;

public class DownLoadListActivity extends BaseActivity {
    private RecyclerView mDownloaded;
    private RecyclerView mDownload;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    private TextView tvContinue, tvFinish;
    private DownloadAdapter01 mDownLoadadapter;//进行中

    private List<DownloadTask> tasks;
    private DownloadManager manager;
    private FileManager fileManager;
    private ArrayList<DownloadInfo> downloads;
    private DownloadAdapter02 mDownLoadedAdapter;
    private DownloadJobListener jobListener = new DownloadJobListener() {
        @Override
        public void onCreated(DownloadInfo info) {
            // Log.d("数据",info+"");
//            if (info != null) {
//
//            }
            tasks.add(0, manager.createTask(info, null));
            mDownLoadadapter.notifyItemInserted(0);
        }

        @Override
        public void onStarted(DownloadInfo info) {
            updateUI();
        }

        @Override
        public void onCompleted(boolean finished, DownloadInfo info) {
            updateUI();
            if (finished) {
                downloads.add(0, info);
                mDownLoadedAdapter.notifyItemInserted(0);
                updateUI1();
            }
        }

    };
    private CustomDialog dialog02;
    private CustomDialog dialog01;
    private List<DownloadInfo> infos;
    private NetSpeedTimer mNetSpeedTimer;
    private String percent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_list);
        ImmersionBar.setStatusBarView(this, status_bar_view);
        ImmersionBar.with(this).statusBarColor(R.color.white).init();
        fileManager = new FileManager(this);
        manager = DownloadManager.getInstance();
        manager.addDownloadJobListener(jobListener);
        tasks = manager.getAllTasks();
        infos = manager.getAllInfo();
        downloads = new ArrayList<>();
        for (DownloadInfo info : infos) {
            if (!info.isFinished()) continue;
            downloads.add(info);
        }
        downloads = new ArrayList<>();

        for (DownloadInfo info : infos) {
            if (!info.isFinished()) continue;
            downloads.add(info);
        }
        initUI();
        tvContinue.setText("进行中(" + tasks.size() + ")");
    }

    private void updateUI() {
        boolean hasTask = tasks.size() > 0;
        if (hasTask) {
            if (mDownload.getVisibility() != View.VISIBLE)
                mDownload.setVisibility(View.VISIBLE);
            tvContinue.setText("进行中(" + tasks.size() + ")");
        } else {
            if (mDownload.getVisibility() == View.VISIBLE)
                mDownload.setVisibility(View.GONE);
            tvContinue.setText("进行中(" + 0 + ")");
        }
    }

    private void updateUI1() {
        boolean hasDownloads = downloads.size() > 0;
        if (!hasDownloads) {
            if (mDownloaded.getVisibility() == View.VISIBLE)
                mDownloaded.setVisibility(View.GONE);
            tvFinish.setText("已完成(0)");
        } else {
            if (mDownloaded.getVisibility() != View.VISIBLE)
                mDownloaded.setVisibility(View.VISIBLE);
            tvFinish.setText("已完成(" + downloads.size() + ")");
        }
    }

    private void initUI() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvContinue = findView(R.id.tvContinue);
        tvFinish = findView(R.id.tvFinish);
        /**
         * 进行中
         */
        mDownload = findView(R.id.download_recycler_view);
        mDownload.setLayoutManager(new LinearLayoutManager(mContext));
        mDownLoadadapter = new DownloadAdapter01();
        mDownload.setAdapter(mDownLoadadapter);
        mDownload.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));

        /**
         * 已完成
         */
        mDownloaded = findView(R.id.downloaded_recycler_view);
        mDownloaded.setLayoutManager(new LinearLayoutManager(mContext));
        mDownLoadedAdapter = new DownloadAdapter02();
        mDownloaded.setAdapter(mDownLoadedAdapter);
        mDownloaded.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));

        updateUI1();
    }


    /**
     * 进行中
     */
    private class DownloadAdapter01 extends RecyclerView.Adapter<DownloadViewHolder01> {

        private LayoutInflater inflater = LayoutInflater.from(mContext);
        long finishedLength;

        @Override
        public DownloadViewHolder01 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.layout_downloading_list_item, parent, false);
            return new DownloadViewHolder01(itemView);
        }

        @Override
        public void onBindViewHolder(DownloadViewHolder01 holder, int position) {
            DownloadTask task = tasks.get(position);
            holder.setKey(task.key);
            task.setListener(holder);
            holder.name.setText(task.name);
            if (infos.size() > 0) {
                for (int i = 0; i < infos.size(); i++) {
                    if (task.key.equals(infos.get(i).key)) {
                        finishedLength = infos.get(i).finishedLength;
                    }
                }
            }
            if (task.size == 0) {
                holder.size.setText(R.string.download_unknown);

            } else {
                holder.size.setText(String.format(Locale.US, "%.1fMB", task.size / 1048576.0f) + " / " + (String.format(Locale.US, "%.1fMB", finishedLength / 1048576.0f)));
            }
            // initNewWork();

            String extension = fileManager.getExtension(task.name);
            holder.icon.setImageResource(GetHeaderImgById(extension));
//            lastTotalRxBytes = getTotalRxBytes();
//            lastTimeStamp = System.currentTimeMillis();
//            new Timer().schedule(task1, 1000, 2000);
            //   new NetWorkSpeedUtils(mContext, mHnadler).startShowNetSpeed();
        }

        private Handler mHnadler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 100:
                        // holder.tvSpeed.setText("当前网速： " + msg.obj.toString());
                        Log.d("当前网速：", msg.obj.toString());
                        break;
                }
                super.handleMessage(msg);
            }
        };

      /*  private long lastTotalRxBytes = 0; // 最后缓存的字节数
        private long lastTimeStamp = 0; // 当前缓存时间
        private Runnable runnable = new Runnable() {

            @Override
            public void run() {
                long nowTotalRxBytes = getTotalRxBytes(); // 获取当前数据总量
                long nowTimeStamp = System.currentTimeMillis(); // 当前时间
                // kb/s
                long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp == lastTimeStamp ? nowTimeStamp : nowTimeStamp
                        - lastTimeStamp));// 毫秒转换
                // tv.setText(String.valueOf(speed) + "kb/s");
                lastTimeStamp = nowTimeStamp;
                lastTotalRxBytes = nowTotalRxBytes;
                handler.postDelayed(runnable, 1000);// 每1秒执行一次runnable.
            }
        };

            private long getTotalRxBytes() {
                // 得到整个手机的流量值
                return TrafficStats.getUidRxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
                        : (TrafficStats.getTotalRxBytes() / 1024);// 转为KB
                // // 得到当前应用的流量值
                // return TrafficStats.getUidRxBytes(getApplicationInfo().uid) ==
                // TrafficStats.UNSUPPORTED ? 0 : (TrafficStats
                // .getUidRxBytes(getApplicationInfo().uid) / 1024);// 转为KB

            }*/

        @Override
        public int getItemCount() {
            return tasks == null ? 0 : tasks.size();
        }

//
//        private long lastTotalRxBytes = 0;
//        private long lastTimeStamp = 0;
//
//        private long getTotalRxBytes() {
//            return TrafficStats.getUidRxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
//        }
//
//        TimerTask task1 = new TimerTask() {
//            @Override
//            public void run() {
//                showNetSpeed();
//            }
//        };
//
//        private void showNetSpeed() {
//
//            long nowTotalRxBytes = getTotalRxBytes();
//            long nowTimeStamp = System.currentTimeMillis();
//            long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
//
//            lastTimeStamp = nowTimeStamp;
//            lastTotalRxBytes = nowTotalRxBytes;
//
//            Message msg = mHandler.obtainMessage();
//            msg.what = 100;
//            msg.obj = String.valueOf(speed) + " kb/s";
//
//            mHandler.sendMessage(msg);//更新界面
//        }
//
//        private Handler mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if (msg.what == NetSpeedTimer.NET_SPEED_TIMER_DEFAULT) {
//                    String speed = (String) msg.obj;
//                    //打印你所需要的网速值，单位默认为kb/s
//                    Log.i("网速", speed + "");
//                }
//            }
//        };

    }

    private class DownloadViewHolder01 extends RecyclerView.ViewHolder implements View.OnClickListener, DownloadListener {

        int state;
        String key;
        ImageView icon;
        TextView name;
        TextView size;
        TextView tvSpeed;
        CircleProgressBar status;
        TextView mDel;

        private DownloadViewHolder01(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.download_icon);
            name = itemView.findViewById(R.id.download_name);
            size = itemView.findViewById(R.id.download_size);
            tvSpeed = itemView.findViewById(R.id.tvSpeed);
            status = itemView.findViewById(R.id.download_status);
            mDel = itemView.findViewById(R.id.delete);
            itemView.setOnClickListener(this);
            mDel.setOnClickListener(this);
            status.setOnClickListener(this);
        }

        void setKey(String key) {
            this.key = key;
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            final DownloadTask task = tasks.get(position);
            if (R.id.download_status == v.getId()) {
                switch (state) {
                    case STATE_PREPARED:
                    case STATE_FAILED:
                        task.start();
                        break;
                    case STATE_PAUSED:
                        task.resume();
                        break;
                    case STATE_WAITING:
                    case STATE_RUNNING:
                        task.pause();
                        break;

                }
            } else if (R.id.delete == v.getId()) {
                dialog01 = new CustomDialog(mContext).builder()
                        .setGravity(Gravity.CENTER)
                        .setTitle("提示", getResources().getColor(R.color.black))//可以不设置标题颜色，默认系统颜色
                        .setSubTitle("是否删除该文件")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog01.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (tasks.contains(task)) {
                                    task.delete();
                                    tasks.remove(task);
                                    mDownLoadadapter.notifyItemRemoved(position);
                                    updateUI();
                                }
                                dialog01.dismiss();
                            }
                        })
                ;

                dialog01.show();
            }
        }


        @Override
        public void onStateChanged(String key, int state) {
            if (!key.equals(this.key)) return;
            this.state = state;
            switch (state) {
                case STATE_PREPARED:
                    status.setStatue(CircleProgressBar.DOWNLOAD_STATUE);
                    break;
                case STATE_FAILED:
                case STATE_PAUSED:
                    status.setStatue(CircleProgressBar.DOWNLOAD_PAUSE);
                    break;
                case STATE_WAITING:
                    status.setStatue(CircleProgressBar.DOWNLOAD_DEF);
                    break;
                case STATE_FINISHED:
                    status.setStatue(CircleProgressBar.DOWNLOAD_FINISH);
                    if (tasks == null) return;
                    int position = getAdapterPosition();
                    if (position < 0) return;
                    DownloadTask task = tasks.get(position);
                    task.clear();
                    tasks.remove(task);
                    mDownLoadadapter.notifyItemRemoved(position);
                    updateUI();
                    break;
            }
        }

        @Override
        public void onProgressChanged(String key, long finishedLength, long contentLength) {

            if (!key.equals(this.key)) return;

            percent = String.format(Locale.US, "%.1f%%", finishedLength * 100.f / Math.max(contentLength, 1));
            percent = percent.substring(0, percent.length() - 1);
            status.setProgress(Float.valueOf(percent));

            if (contentLength == 0) {
                size.setText(R.string.download_unknown);
            } else {
                percent = String.format(Locale.US, "%.1f%%", finishedLength * 100.f / Math.max(contentLength, 1));
                percent = percent.substring(0, percent.length() - 1);
                status.setProgress(Float.valueOf(percent));
                status.setStatue(CircleProgressBar.DOWNLOAD_STATUE);
                size.setText(String.format(Locale.US, "%.1fMB", contentLength / 1048576.0f) + " / " + (String.format(Locale.US, "%.1fMB", finishedLength / 1048576.0f)));
            }
        }
    }

    /**
     * 进行中结束
     */

    /**
     * 已完成开始
     */
    private class DownloadAdapter02 extends RecyclerView.Adapter<DownloadViewHolder02> {

        private LayoutInflater inflater = LayoutInflater.from(mContext);
        private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        @Override
        public DownloadViewHolder02 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.layout_downloaded_list_item, parent, false);
            return new DownloadViewHolder02(itemView);
        }

        @Override
        public void onBindViewHolder(DownloadViewHolder02 holder, int position) {
            DownloadInfo info = downloads.get(position);
            holder.name.setText(info.name);
            holder.timestamp.setText(format.format(new Date(info.createTime)));
            holder.size.setText(info.extras);
            String extension = fileManager.getExtension(info.name);
            holder.icon.setImageResource(GetHeaderImgById(extension));
        }


        @Override
        public int getItemCount() {
            return downloads == null ? 0 : downloads.size();
        }
    }

    private class DownloadViewHolder02 extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView icon;
        TextView name;
        TextView size;
        TextView timestamp;
        TextView mDel;
        LinearLayout llItem;

        DownloadViewHolder02(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.download_icon);
            name = itemView.findViewById(R.id.download_name);
            timestamp = itemView.findViewById(R.id.download_timestamp);
            size = itemView.findViewById(R.id.download_size);
            mDel = itemView.findViewById(R.id.delete);
            llItem = itemView.findViewById(R.id.llItem);
            itemView.setOnClickListener(this);
            mDel.setOnClickListener(this);
            llItem.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            if (R.id.llItem == v.getId()) {
                if (Utils.isFastClick()) {
                    DownloadInfo info = downloads.get(position);
                    File file = new File(info.path);
                    if (!file.exists()) return;
                    /* 取得扩展名 */
                    String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
                    if (end.equals("zip")) {
                        showOnlineDialog dialog01 = new showOnlineDialog();
                        dialog01.CenterDialog(mContext, String.valueOf(info.id), info.path, info.name, position);

                    } else {
                        fileManager.openFile(mContext, info.path, info.name);
                    }
                }

            } else if (R.id.delete == v.getId()) {
                dialog02 = new CustomDialog(mContext).builder()
                        .setGravity(Gravity.CENTER)
                        .setTitle("提示", getResources().getColor(R.color.black))//可以不设置标题颜色，默认系统颜色
                        .setSubTitle("是否删除该文件")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog02.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadInfo info = downloads.get(position);
                                manager.delete(info);
                                downloads.remove(info);
                                mDownLoadedAdapter.notifyItemRemoved(position);
                                updateUI1();
                                dialog02.dismiss();
                            }
                        })
                ;

                dialog02.show();
            }
        }


    }

    /**
     * 已完成结束
     */
    @Override
    public void onResume() {
        super.onResume();
        for (DownloadTask task : tasks) {
            task.resumeListener();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (DownloadTask task : tasks) {
            task.pauseListener();
        }
    }

    @Override
    public void onDestroy() {
        manager.removeDownloadJobListener(jobListener);
        for (DownloadTask task : tasks) {
            task.clear();
        }
        tasks.clear();
        tasks = null;
        if (null != mNetSpeedTimer) {
            mNetSpeedTimer.stopSpeedTimer();
        }
        super.onDestroy();
    }


}
