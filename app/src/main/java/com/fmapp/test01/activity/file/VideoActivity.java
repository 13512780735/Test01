package com.fmapp.test01.activity.file;


import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fmapp.test01.R;
import com.fmapp.test01.base.BaseActivity;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;


import butterknife.BindView;

public class VideoActivity extends BaseActivity {
    @BindView(R.id.nice_video_player)
    NiceVideoPlayer mNiceVideoPlayer;
    private String uri;
    private String name;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        uri = getIntent().getStringExtra("uri");
        name = getIntent().getStringExtra("name");
        Log.d("视频路径：", uri);
        Log.d("视频名称：", name);
        init();
    }

    private void init() {
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
        videoUrl = uri;
        mNiceVideoPlayer.setUp(videoUrl, null);
        TxVideoPlayerController controller = new TxVideoPlayerController(this);
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoUrl);
        Bitmap bitmap = media.getFrameAtTime();
        controller.setTitle(name);
        controller.imageView().setImageBitmap(bitmap);
        controller.setLenght(98000);
        mNiceVideoPlayer.setController(controller);
    }


    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }
}
