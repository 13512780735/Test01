package com.fmapp.test01.activity.cloud;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arialyy.aria.core.Aria;
import com.fmapp.test01.R;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.network.model.BaseResponse;
import com.fmapp.test01.network.model.DownLoadModel;
import com.fmapp.test01.network.model.SvipDownModel;
import com.fmapp.test01.network.util.DataResultException;
import com.fmapp.test01.network.util.RetrofitUtil;
import com.fmapp.test01.utils.StatusBarUtil;

import java.io.File;
import java.sql.SQLException;

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
                    Bundle bundle = new Bundle();
                    bundle.putString("link", link);
                    bundle.putString("ext", ext);
                    bundle.putString("id", downLoadModel.getId());
                    bundle.putString("name", downLoadModel.getName());
                    bundle.putString("size", downLoadModel.getSize());
                    /*将任务添加到下载队列，下载器会自动开始下载*/
                    addTask(link);
                    toActivity(DownLoadListActivity.class, bundle);

                }
            }

        });
    }

    private void addTask(String link) {
        Aria.download(this).load(link).setFilePath(Environment.getExternalStorageDirectory() + "/Download/" + downLoadModel.getName())
                .create();

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
