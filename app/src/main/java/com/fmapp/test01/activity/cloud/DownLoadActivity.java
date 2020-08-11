package com.fmapp.test01.activity.cloud;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.HttpOption;
import com.arialyy.aria.core.common.RequestEnum;
import com.arialyy.aria.core.download.m3u8.M3U8VodOption;
import com.arialyy.aria.core.processor.IHttpFileLenAdapter;
import com.arialyy.aria.core.processor.IKeyUrlConverter;
import com.fmapp.test01.R;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.download.FileListEntity;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.DownLoadModel;
import com.fmapp.test01.network.model.SvipDownModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.StatusBarUtil;


import java.io.File;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

import static com.fmapp.test01.utils.com.GetHeaderImgById;

/**
 * 文件下载
 */
public class DownLoadActivity extends BaseActivity {
    @BindView(R.id.tv_back)
    ImageView mBack;
    @BindView(R.id.ivPic)
    ImageView ivPic;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvSize)
    TextView tvSize;
    @BindView(R.id.tvContent)
    TextView tvContent;
    private int id;
    private DownLoadModel downLoadModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        int color = getResources().getColor(R.color.white);
        StatusBarUtil.setColor(this, color, 0);
        StatusBarUtil.setLightMode(this);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        initUI();
        getData();
    }


    private void getData() {
        LoaddingShow();
        RetrofitUtil.getInstance().filedetail(token, id, new Subscriber<BaseResponse<DownLoadModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoaddingDismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    showToast(resultException.getMsg());
                }
            }

            @Override
            public void onNext(BaseResponse<DownLoadModel> baseResponse) {
                LoaddingDismiss();
                if (baseResponse.getStatus() == 1) {
                    downLoadModel = baseResponse.getData();
                    ivPic.setImageResource(GetHeaderImgById(baseResponse.getData().getExt()));
                    tvName.setText(baseResponse.getData().getName());
                    tvSize.setText("文件大小：" + baseResponse.getData().getSize());
                    tvContent.setText("本文件内容由" + baseResponse.getData().getUname() + "自行上传，并不代表本站立场");
                } else showToast(baseResponse.getMsg());
            }
        });
    }

    private void initUI() {
        setBackView();
        mBack.setImageDrawable(getResources().getDrawable(R.mipmap.icon_back));
        setTitle("文件下载");
    }

    @OnClick({R.id.llVip, R.id.rlCloud, R.id.rlStar, R.id.rlSvipDown, R.id.rlQuanDown})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llVip:
                break;
            case R.id.rlCloud:
                toCloud();

                break;
            case R.id.rlStar:
                toStar();
                break;
            case R.id.rlSvipDown:
            case R.id.rlQuanDown:
                toDownLoad();
                break;
        }
    }

    private void toDownLoad() {
        LoaddingShow();
        RetrofitUtil.getInstance().svipdown(token, id, new Subscriber<BaseResponse<SvipDownModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoaddingDismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    showToast(resultException.getMsg());
                }
            }

            @Override
            public void onNext(BaseResponse<SvipDownModel> baseResponse) {
                LoaddingDismiss();
                if (baseResponse.getStatus() == 1) {
                    String link = baseResponse.getData().getLink();
                    String ext = baseResponse.getData().getExtension();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("link", link);
//                    bundle.putString("ext", ext);
//                    bundle.putString("id", downLoadModel.getId());
//                    bundle.putString("name", downLoadModel.getName());
//                    bundle.putString("size", downLoadModel.getSize());
                    /*将任务添加到下载队列，下载器会自动开始下载*/
                    addTask(link, ext);
                    toActivity(DownLoadListActivity.class);

                }
            }

        });
    }

    private void addTask(String link, String ext) {
        Log.d("下载链接", link);
        // String url = "http://hzdown.muzhiwan.com/2017/05/08/nl.noio.kingdom_59104935e56f0.apk";
        // String url = "https://40402.xc.acq42.com/xiaz/%E7%8E%8B%E8%80%85%E8%8D%A3%E8%80%80%E4%B8%8B%E8%BD%BD@131_362996.exe";
//        String url = link;
//        FileListEntity entity = new FileListEntity();
//        entity.name = downLoadModel.getName();
//        entity.key = url;
//        entity.ext = downLoadModel.getExt();
//        entity.id = downLoadModel.getId();
//        entity.downloadPath = Environment.getExternalStorageDirectory() + "/Download/" + downLoadModel.getName();
//        final String[] url = new String[1];

        //   String url="https://webimg.fmapp.com/dlr2.php?t=1597110492";
         String url=link;
        // String url="http://s2.d28.ihuolong.net/dlios.php?Y2IwNXpmQk5yUFpLaG5IbVk1UkcxK2FCU1JLOVM3SGdTZ0M2ZkRiMTdjZUpOaG1CclQxbTBHVituZlBZOURQaW1nOElHdUJiV3c3Z2piRElvcHNEVFg4alQrL0VoOFRabjVjTDB5R0Y4d3pqR2tuNWowcngrUm5uYXJ3Vk9LMUtZMGl2Q1lyTnpET1Q5L2FXaXpYUm5qNlgwZnlZdkJYL3d0Y3JiR0tFNkYycDJ4TjhMQXRmT3BjZTQ3TmZmdCtOUGcwZEFUNnQvVmdvSFBYbkdXbmttbVRhUERKYmo0TEtVUWV5NFBHeGNLSjV5VE1uR1FJNS94a1ZtV3R2YWZN";
        HttpOption option = new HttpOption();
        option.addHeader("token", token);
        //option.setFileLenAdapter(new HttpFileLenAdapter());
        Aria.download(this)
                .load(url)
                .option(option)
                .setFilePath(Environment.getExternalStorageDirectory() + "/Download/" + downLoadModel.getName())
                .create();
    }

    private static class HttpFileLenAdapter implements IHttpFileLenAdapter {
        @Override
        public long handleFileLen(Map<String, List<String>> headers) {
            List<String> sLength = headers.get("Content-Length");
            if (sLength == null || sLength.isEmpty()) {
                return -1;
            }
            String temp = sLength.get(0);
            return Long.parseLong(temp);
        }
    }

    /**
     * 收藏到星标
     */
    private void toStar() {
        LoaddingShow();
        RetrofitUtil.getInstance().cltfile(token, id, new Subscriber<BaseResponse<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoaddingDismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    showToast(resultException.getMsg());
                }
            }

            @Override
            public void onNext(BaseResponse<String> baseResponse) {
                LoaddingDismiss();
                if (baseResponse.getStatus() == 1) {
                    showToast(baseResponse.getMsg());
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    //操作台文件列表数据（包括永久空间）
    private void toCloud() {
        LoaddingShow();
        RetrofitUtil.getInstance().tospro(token, id, new Subscriber<BaseResponse<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoaddingDismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    showToast(resultException.getMsg());
                }
            }

            @Override
            public void onNext(BaseResponse<String> baseResponse) {
                LoaddingDismiss();
                if (baseResponse.getStatus() == 1) {
                    showToast(baseResponse.getMsg());
                } else {
                    showToast(baseResponse.getMsg());
                }

            }
        });
    }
}
