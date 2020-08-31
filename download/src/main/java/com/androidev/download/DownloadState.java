package com.androidev.download;

/**
 * Created by 4ndroidev on 16/10/6.
 */
public class DownloadState {

    public static final int STATE_PREPARED = 0;//准备中
    public static final int STATE_RUNNING = 1; //下载中
    public static final int STATE_FINISHED = 2;  //完成
    public static final int STATE_FAILED = 3;  //失败
    public static final int STATE_PAUSED = 4;//暂停
    public static final int STATE_WAITING = 5;//等待
}
