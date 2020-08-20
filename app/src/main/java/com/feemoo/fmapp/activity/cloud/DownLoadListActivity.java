package com.feemoo.fmapp.activity.cloud;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidev.download.DownloadInfo;
import com.androidev.download.DownloadJobListener;
import com.androidev.download.DownloadListener;
import com.androidev.download.DownloadManager;
import com.androidev.download.DownloadTask;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.file.showOnlineDialog;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.download.util.FileManager;
import com.feemoo.fmapp.utils.CustomDialog;
import com.feemoo.fmapp.utils.Utils;
import com.feemoo.fmapp.widght.CircleProgressBar;
import com.feemoo.fmapp.widght.SwipeItemLayout;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

import static com.androidev.download.DownloadState.STATE_FAILED;
import static com.androidev.download.DownloadState.STATE_FINISHED;
import static com.androidev.download.DownloadState.STATE_PAUSED;
import static com.androidev.download.DownloadState.STATE_PREPARED;
import static com.androidev.download.DownloadState.STATE_RUNNING;
import static com.androidev.download.DownloadState.STATE_WAITING;
import static com.feemoo.fmapp.utils.com.GetHeaderImgById;

public class DownLoadListActivity extends BaseActivity {
    private RecyclerView mDownloaded;
    private RecyclerView mDownload;
    @BindView(R.id.tv_back)
    ImageView mBack;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_list);
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
        setBackView();
        mBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        setTitle("下载列表");
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
            String extension = fileManager.getExtension(task.name);
            holder.icon.setImageResource(GetHeaderImgById(extension));
        }

        @Override
        public int getItemCount() {
            return tasks == null ? 0 : tasks.size();
        }
    }

    private class DownloadViewHolder01 extends RecyclerView.ViewHolder implements View.OnClickListener, DownloadListener {

        int state;
        String key;
        ImageView icon;
        TextView name;
        TextView size;
        CircleProgressBar status;
        TextView mDel;

        private DownloadViewHolder01(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.download_icon);
            name = itemView.findViewById(R.id.download_name);
            size = itemView.findViewById(R.id.download_size);
            status = itemView.findViewById(R.id.download_status);
            mDel = itemView.findViewById(R.id.delete);
            itemView.setOnClickListener(this);
            mDel.setOnClickListener(this);
            status.setOnClickListener(this);
            // size.setText(String.format(Locale.US, "%.1fMB", tasks.get(getAdapterPosition()).size / 1048576.0f) + " / " + (String.format(Locale.US, "%.1fMB", finishedLength / 1048576.0f)));
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
                        .setNegativeButton("取消", R.color.button_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", R.color.button_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (tasks.contains(task)) {
                                    task.delete();
                                    tasks.remove(task);
                                    mDownLoadadapter.notifyItemRemoved(position);
                                    updateUI();
                                }
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
            // status.setText(String.format(Locale.US, "%.1f%%", finishedLength * 100.f / Math.max(contentLength, 1)));
            status.setProgress(finishedLength * 100.f / Math.max(contentLength, 1));
            status.setStatue(CircleProgressBar.DOWNLOAD_STATUE);

            if (contentLength == 0) {
                size.setText(R.string.download_unknown);
            } else {
                status.setProgress(finishedLength * 100.f / Math.max(contentLength, 1));
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
            //holder.size.setText(String.format(Locale.US, "%.1fMB", info.contentLength / 1048576.0f));
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
                        .setNegativeButton("取消", R.color.button_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("确定", R.color.button_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DownloadInfo info = downloads.get(position);
                                manager.delete(info);
                                downloads.remove(info);
                                mDownLoadedAdapter.notifyItemRemoved(position);
                                updateUI1();
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
        super.onDestroy();
        manager.removeDownloadJobListener(jobListener);
        for (DownloadTask task : tasks) {
            task.clear();
        }
        tasks.clear();
        tasks = null;
    }

}
