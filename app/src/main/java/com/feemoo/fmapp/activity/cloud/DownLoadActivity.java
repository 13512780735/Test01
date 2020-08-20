package com.feemoo.fmapp.activity.cloud;


import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidev.download.DownloadInfo;
import com.androidev.download.DownloadManager;
import com.androidev.download.DownloadTask;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.DownLoadModel;
import com.feemoo.fmapp.network.model.SvipDownModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;

import static com.feemoo.fmapp.utils.com.GetHeaderImgById;

/**
 * 文件下载
 */
public class DownLoadActivity extends BaseActivity {
    private final int REQUEST_PERMISSION_CODE = 1;//请求码
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
    private List<DownloadTask> tasks;
    private String key;
    private List<DownloadTask> mContinueData;
    private List<DownloadInfo> mFinishData;

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
                if ("1".equals(baseResponse.getStatus())) {
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
                //checkPermission();

                String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (!EasyPermissions.hasPermissions(DownLoadActivity.this, perms)) {
                    EasyPermissions.requestPermissions(DownLoadActivity.this, "请允许文件读写权限，否则无法正常使用本应用！", 10086, perms);
                } else {
                    toDownLoad();
                }

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
                if ("1".equals(baseResponse.getStatus())) {
                    String link = baseResponse.getData().getLink();
                    String ext = baseResponse.getData().getExtension();
                    addTask(link, ext);
                }
            }

        });
    }

    private void addTask(String link, String ext) {
        DownloadManager controller = DownloadManager.getInstance();
        tasks = new ArrayList<>();
        tasks.add(controller.newTask(Integer.parseInt(downLoadModel.getId()), link, downLoadModel.getName()).extras(downLoadModel.getSize()).create());
        key = tasks.get(0).key;
        mFinishData = controller.getAllInfo();
        boolean flag = false;
        for (int i = 0; i < mFinishData.size(); i++) {
            if (mFinishData.get(i).key.contains(key)) {
                flag = true;
                break;
            } else {
                flag = false;
            }
        }

        if (flag) {
            showToast("该文件已经在下载列表中");
        } else {
            tasks.get(0).start();
            toActivity(DownLoadListActivity.class);
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
                if ("1".equals(baseResponse.getStatus())) {
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
                if ("1".equals(baseResponse.getStatus())) {
                    showToast(baseResponse.getMsg());
                } else {
                    showToast(baseResponse.getMsg());
                }

            }
        });
    }

}
