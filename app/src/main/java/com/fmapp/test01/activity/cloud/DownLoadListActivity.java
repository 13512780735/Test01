package com.fmapp.test01.activity.cloud;


import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;


import com.fmapp.test01.R;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.download.ListAdapter;
import com.fmapp.test01.download.filedownloader.download.DownLoadManager;
import com.fmapp.test01.download.filedownloader.download.DownLoadService;
import com.fmapp.test01.download.filedownloader.download.TaskInfo;


import butterknife.BindView;

public class DownLoadListActivity extends BaseActivity {
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private ListView listview;
    @BindView(R.id.tv_back)
    ImageView mBack;
    private String link, ext, name, size, id;
    /*使用DownLoadManager时只能通过DownLoadService.getDownLoadManager()的方式来获取下载管理器，不能通过new DownLoadManager()的方式创建下载管理器*/
    private DownLoadManager manager;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_list);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        link = bundle.getString("link");
        ext = bundle.getString("ext");
        name = bundle.getString("name");
        size = bundle.getString("size");

        listview = (ListView) this.findViewById(R.id.listView);
        //  handler.sendEmptyMessageDelayed(1, 500);
        manager = DownLoadService.getDownLoadManager();
        Log.d("链接",link);
        /*设置用户ID，客户端切换用户时可以显示相应用户的下载任务*/
        /*断点续传需要服务器的支持，设置该项时要先确保服务器支持断点续传功能*/
        manager.setSupportBreakpoint(true);
        adapter = new ListAdapter(DownLoadListActivity.this, manager);
        listview.setAdapter(adapter);
        initUI();

    }


    private void initUI() {
        setBackView();
        mBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        setTitle("下载列表");
//        TaskInfo info = new TaskInfo();
//        info.setFileName(name);
//        /*服务器一般会有个区分不同文件的唯一ID，用以处理文件重名的情况*/
//        info.setTaskID(name);
//        info.setOnDownloading(true);
//        /*将任务添加到下载队列，下载器会自动开始下载*/
//        manager.addTask("AZNQ0825.mp4.zip", "http://s.d28.ihuolong.net/dlios.php?MDIwNFVvQjRhVVV3TFZMQWZVNkxlSUZpOFZUc3ZHVHl0WktwTzVRSEgwbnUrWU9LemNJRFVXSXF1aHd4bi9DR2RtVk5aekFhUzFNTUdaN242cDQrQ2RiUUUybnYvNU1kN1RJVFFZR1Y4a05MSTEzRDFiTitEOG0yenBNM2FldzVBSnluRFJLZm40Y25HMDBtQW9ZeXhxcWtWQ3pFSGFjR3YyYjA0MEg1dkJ3a0FoMW9vZHR1dnBLdmNybURPOXU3Rnh0YVZJQmJqbnZMek5IWlJjWDdiUGdVd2FHZDRqSWV3TEZicThGWkN2MVR6VithZHA4TCtjV2JiUzlMWlIyM1BsYlE%3D", "AZNQ0825.mp4.zip");
//        adapter.addItem(info);

        TaskInfo info = new TaskInfo();
        info.setFileName(name);
        /*服务器一般会有个区分不同文件的唯一ID，用以处理文件重名的情况*/
        info.setTaskID(id);
        info.setOnDownloading(true);
        adapter.addItem(info);
    }

  /*  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            *//*获取下载管理器*//*

        }
    };*/
}
