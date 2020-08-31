package com.feemoo.fmapp.activity.select;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidev.download.DownloadInfo;
import com.androidev.download.DownloadManager;
import com.androidev.download.DownloadTask;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.cloud.DownLoadListActivity;
import com.feemoo.fmapp.adapter.JXDetailAdapter;
import com.feemoo.fmapp.adapter.JXDetailsImgAdapter;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.model.JXLikeModel;
import com.feemoo.fmapp.network.model.JxDetailsModel;
import com.feemoo.fmapp.network.model.SvipDownModel;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.widght.BorderTextView;
import com.feemoo.fmapp.widght.CircleImageView;
import com.gyf.immersionbar.ImmersionBar;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

import static com.feemoo.fmapp.utils.com.GetHeaderImgById;

public class ProjectDetailsActivity extends BaseActivity {
    @BindView(R.id.mIvHeader)
    ImageView mIvHeader;//顶部
    @BindView(R.id.tvTitle)
    TextView tvTitle;//产品名称
    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;//用户头像
    @BindView(R.id.tvName01)
    TextView tvUserName;//用户姓名
    @BindView(R.id.tvDate)
    TextView tvDate;//时间
    @BindView(R.id.mRichTextView)
    TextView mRichTextView;//富文本
    @BindView(R.id.mRichTextView2)
    TextView mRichTextView2;//富文本
    @BindView(R.id.llDown)
    LinearLayout llDown;//是否下载项
    @BindView(R.id.fileFormat)
    ImageView fileFormat;//文件格式
    @BindView(R.id.tvName)
    TextView tvName;//文件名称
    @BindView(R.id.tv_move)
    BorderTextView tv_move;//文件名称
    @BindView(R.id.tvContent)
    TextView tvContent;//文件大小
    @BindView(R.id.tvDown01)
    BorderTextView tvDown01;//文件下载
    @BindView(R.id.tvDown)
    BorderTextView tvDown;//文件下载
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;//文件下载推荐
    @BindView(R.id.mRecycleViewImg)
    RecyclerView mRecycleViewImg;//分类id 1 PPT和26文库的
    @BindView(R.id.tv_share)
    ImageView tv_share;//文件分享
    @BindView(R.id.ra_userful)
    RadioButton ra_userful;//有用
    @BindView(R.id.ra_userless)
    RadioButton ra_userless;//没用
    @BindView(R.id.mRg)
    RadioGroup mRg;//没用
    List<JxDetailsModel.BsfilesBean> bsfilesBeanList = new ArrayList<>();
    List<String> imgs = new ArrayList<>();
    private String id;
    private JXDetailAdapter mAdapter;
    private JXDetailsImgAdapter mImgAdapter;
    private String st;
    private List<DownloadTask> tasks;
    private String key;
    private List<DownloadInfo> mFinishData;

    private void refresh(JxDetailsModel jxDetailsModel) {
        if (jxDetailsModel.getBsfiles().size() > 0) {
            bsfilesBeanList = jxDetailsModel.getBsfiles();
            mAdapter.setNewData(bsfilesBeanList);
            mAdapter.notifyDataSetChanged();
        }
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIvHeader.getLayoutParams();
        int width1 = width;
        params.width = width1;
        params.height = width1 / 2;
        mIvHeader.setLayoutParams(params);


        Glide.with(mContext).load(jxDetailsModel.getCover()).into(mIvHeader);
        tvTitle.setText(jxDetailsModel.getName());
        Glide.with(mContext).load(jxDetailsModel.getFace()).into(ivAvatar);
        tvUserName.setText(jxDetailsModel.getUsername());
        tvDate.setText(jxDetailsModel.getTime());
        // mRichTextView.
        fileFormat.setImageResource(GetHeaderImgById(jxDetailsModel.getExt()));
        tvName.setText(jxDetailsModel.getName());
        tvContent.setText(jxDetailsModel.getSize());
        if (jxDetailsModel.getSt1().equals("1")) {
            ra_userful.setChecked(true);
        } else {
            ra_userful.setChecked(false);
        }
        if (jxDetailsModel.getSt2().equals("1")) {
            ra_userless.setChecked(true);
        } else {
            ra_userless.setChecked(false);
        }
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(jxDetailsModel.getLink());
                showToast("本文件分享地址已复制剪切板，请前往粘贴使用");
            }
        });
        String ptype = jxDetailsModel.getPtype();
        if ("33".equals(ptype)) {
            llDown.setVisibility(View.GONE);
            tvDown.setVisibility(View.INVISIBLE);
            tv_move.setVisibility(View.VISIBLE);

        } else {
            llDown.setVisibility(View.VISIBLE);
            tvDown.setVisibility(View.VISIBLE);
            tv_move.setVisibility(View.GONE);
        }
        if ("1".equals(ptype) || "26".equals(ptype)) {
            mRecycleViewImg.setVisibility(View.VISIBLE);
            mRichTextView.setVisibility(View.GONE);
            imgs = jxDetailsModel.getImgs();
            mImgAdapter.setNewData(imgs);
            mImgAdapter.notifyDataSetChanged();
        } else {
            mRichTextView.setVisibility(View.VISIBLE);
            mRecycleViewImg.setVisibility(View.GONE);
            RichText.initCacheDir(this);
            RichText.fromHtml(jxDetailsModel.getVhtml()).into(mRichTextView);

        }
        tv_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMove(id);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", bsfilesBeanList.get(position).getId());
                toActivity(ProjectDetailsActivity.class, bundle);
            }
        });

        tvDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDownLoad(jxDetailsModel);
            }
        });
        tvDown01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDownLoad(jxDetailsModel);
            }
        });
    }

    /**
     * 点赞 /点踩
     *
     * @param st
     */
    private void toLike(String st) {
        LoaddingShow();
        RetrofitUtil.getInstance().jxlike(token, id, st, new Subscriber<BaseResponse<JXLikeModel>>() {
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
            public void onNext(BaseResponse<JXLikeModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    String st1 = baseResponse.getData().getSt1();
                    String st2 = baseResponse.getData().getSt2();

                    if (st1.equals("1")) {
                        ra_userful.setChecked(true);
                    } else {
                        ra_userful.setChecked(false);
                    }
                    if (st2.equals("1")) {
                        ra_userless.setChecked(true);
                    } else {
                        ra_userless.setChecked(false);
                    }
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });
    }

    /**
     * 技术教程获取更多
     *
     * @param id
     */
    private void toMove(String id) {
        LoaddingShow();
        RetrofitUtil.getInstance().gethfcon(token, id, new Subscriber<BaseResponse<String>>() {
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
                    tv_move.setVisibility(View.GONE);
                    mRichTextView2.setVisibility(View.VISIBLE);
                    RichText.from(baseResponse.getData()).bind(this)
                            .showBorder(false)
                            .scaleType(ImageHolder.ScaleType.fit_center)
                            .clickable(false)
                            .size(ImageHolder.WRAP_CONTENT, ImageHolder.WRAP_CONTENT)
                            .into(mRichTextView2);
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });


    }

    /**
     * 下载
     *
     * @param jxDetailsModel
     */
    private void toDownLoad(JxDetailsModel jxDetailsModel) {
        LoaddingShow();
        RetrofitUtil.getInstance().jxdown(token, id, new Subscriber<BaseResponse<SvipDownModel>>() {
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
                    addTask(link, ext, jxDetailsModel);
                }
            }

        });
    }

    private void addTask(String link, String ext, JxDetailsModel jxDetailsModel) {
        DownloadManager controller = DownloadManager.getInstance();
        tasks = new ArrayList<>();
        String name = jxDetailsModel.getName() + "." + jxDetailsModel.getExt();
        tasks.add(controller.newTask(Integer.parseInt(jxDetailsModel.getId()), link, name).extras(jxDetailsModel.getSize()).create());
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        ImmersionBar.with(this).statusBarView(R.id.top_view)
                .fullScreen(true)
                .addTag("PicAndColor")
                .init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
        }
        Log.d("id", id + "");
        initUI();
        initData();
    }

    private void initData() {
        LoaddingShow();
        RetrofitUtil.getInstance().jxdetail(token, id, new Subscriber<BaseResponse<JxDetailsModel>>() {
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
            public void onNext(BaseResponse<JxDetailsModel> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    JxDetailsModel jxDetailsModel = baseResponse.getData();
                    refresh(jxDetailsModel);
                } else {
                    showToast(baseResponse.getMsg());
                }
            }
        });


//        String url = AppConst.BASE_URL + "choice/jxdetail";
//        OkHttpClient client = new OkHttpClient();
//        RequestBody formBody = new FormBody.Builder()
//                .add("id", id)
//                .build();
//        Request request = new Request
//                .Builder()
//                .url(url)//要访问的链接
//                .addHeader("token", token)
//                .post(formBody)
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LoaddingDismiss();
//            }
//
//            @Override
//            public void onResponse(Call call, okhttp3.Response response) throws IOException {
//                LoaddingDismiss();
//                try {
//                    JSONObject object = new JSONObject(response.body().string());
//                    String status = String.valueOf(object.optInt("status"));
//                    String msg = object.optString("msg");
//                    if ("1".equals(status)) {
//                        JxDetailsModel jxDetailsModel = new JxDetailsModel();
//                        jxDetailsModel.setId(object.optJSONObject("data").optString("id"));
//                        jxDetailsModel.setPtype(object.optJSONObject("data").optString("ptype"));
//                        jxDetailsModel.setName(object.optJSONObject("data").optString("name"));
//                        jxDetailsModel.setFshort(object.optJSONObject("data").optString("fshort"));
//                        jxDetailsModel.setExtension(object.optJSONObject("data").optString("extension"));
//                        jxDetailsModel.setSize(object.optJSONObject("data").optString("size"));
//                        jxDetailsModel.setTime(object.optJSONObject("data").optString("time"));
//                        jxDetailsModel.setCover(object.optJSONObject("data").optString("cover"));
//                        jxDetailsModel.setBasesize(object.optJSONObject("data").optString("basesize"));
//                        jxDetailsModel.setFace(object.optJSONObject("data").optString("face"));
//                        jxDetailsModel.setUsername(object.optJSONObject("data").optString("username"));
//                        jxDetailsModel.setLink(object.optJSONObject("data").optString("link"));
//                        jxDetailsModel.setExt(object.optJSONObject("data").optString("ext"));
//                        jxDetailsModel.setSt1(object.optJSONObject("data").optString("st1"));
//                        jxDetailsModel.setSt2(object.optJSONObject("data").optString("st2"));
//                        jxDetailsModel.setVhtml(object.optJSONObject("data").optString("vhtml"));
//                        List<String> imgs = new ArrayList<>();
//                        JSONArray array = object.optJSONObject("data").optJSONArray("imgs");
//                        if (array.length() > 0) {
//                            for (int i = 0; i < array.length(); i++) {
//                                imgs.add(array.get(i).toString());
//                            }
//                        }
//                        jxDetailsModel.setImgs(imgs);
//                        List<JxDetailsModel.BsfilesBean> jx = new ArrayList<>();
//                        JSONArray array01 = object.optJSONObject("data").optJSONArray("bsfiles");
//                        if (array01.length() > 0) {
//                            for (int i = 0; i < array01.length(); i++) {
//                                JSONObject object1 = array01.optJSONObject(i);
//                                JxDetailsModel.BsfilesBean bsfilesBean = new JxDetailsModel.BsfilesBean();
//                                bsfilesBean.setId(object1.optString("id"));
//                                bsfilesBean.setImg(object1.optString("img"));
//                                bsfilesBean.setName(object1.optString("name"));
//                                jx.add(bsfilesBean);
//                            }
//                        }
//                        jxDetailsModel.setBsfiles(jx);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                refresh(jxDetailsModel);
//                            }
//                        });
//
//                    } else {
//                        showToast(msg);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }


    private void initUI() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new JXDetailAdapter(R.layout.jxdetails_items, bsfilesBeanList);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        mRecycleViewImg.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mImgAdapter = new JXDetailsImgAdapter(R.layout.jxdetails_items_img, imgs);
        mRecycleViewImg.setAdapter(mImgAdapter);
        mImgAdapter.notifyDataSetChanged();
        ra_userful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st = "1";
                toLike(st);
            }
        });
        ra_userless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st = "0";
                toLike(st);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.clear(this);
    }
}
