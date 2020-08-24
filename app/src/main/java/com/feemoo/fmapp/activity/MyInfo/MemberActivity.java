package com.feemoo.fmapp.activity.MyInfo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.feemoo.fmapp.R;
import com.feemoo.fmapp.activity.login.Register02Activity;
import com.feemoo.fmapp.base.BaseActivity;
import com.feemoo.fmapp.network.Appconst.AppConst;
import com.feemoo.fmapp.network.model.BaseResponse;
import com.feemoo.fmapp.network.util.DataResultException;
import com.feemoo.fmapp.network.util.RetrofitUtil;
import com.feemoo.fmapp.utils.GlideEngine;
import com.feemoo.fmapp.utils.ImageUtils;
import com.feemoo.fmapp.widght.BorderTextView;
import com.feemoo.fmapp.widght.CircleImageView;
import com.feemoo.fmapp.widght.dialog.ActionSheetDialog;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Subscriber;

/**
 * 个人中心
 */
public class MemberActivity extends BaseActivity {
    @BindView(R.id.ivHeader)
    CircleImageView mIvHeader;
    @BindView(R.id.mTvSave)
    BorderTextView mTvSave;
    private Bitmap bitmap;
    private String avatar;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            avatar = bundle.getString("avatarImg");
        }
        initUI();
    }

    private List<String> mCloudData = new ArrayList<>();

    private void initUI() {
        Glide.with(mContext).load(avatar).into(mIvHeader);
    }

    @OnClick({R.id.ivHeader, R.id.mTvSave})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivHeader:
                new ActionSheetDialog(mContext)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .setTitle("请选择上传类型")
                        .addSheetItem("手机相册选择", ActionSheetDialog.SheetItemColor.Blue,
                                which -> EasyPhotos.createAlbum(MemberActivity.this, false, GlideEngine.getInstance())
                                        .start(101))
                        .addSheetItem("相机", ActionSheetDialog.SheetItemColor.Blue,
                                which -> EasyPhotos.createCamera(MemberActivity.this)
                                        .setFileProviderAuthority("com.example.test01.fileprovider")
                                        .start(101)).show();
                break;
            case R.id.mTvSave:
                if (TextUtils.isEmpty(imgUrl)) {
                    showToast("请上传头像");
                } else {
                    refresh(imgUrl);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            //相机或相册回调
            if (requestCode == 101) {
                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                File file = new File(resultPhotos.get(0).path);
                bitmap = BitmapFactory.decodeFile(file.toString());
                byte[] content = ImageUtils.getBitmapByte(bitmap);
                upload(content, file, bitmap);
                //  GlideEngine.getInstance().loadGif(mContext, resultPhotos.get(0).uri, mIvHeader);
                return;
            }
        } else if (RESULT_CANCELED == resultCode) {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        }
    }

    private void upload(byte[] content, File file, Bitmap bitmap) {
        LoaddingShow();
        String url = "https://ucgimg.fmapp.com/img_server/kind/php/img_up.php";
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        //第一个参数要与Servlet中的一致
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
        ;
        MultipartBody multipartBody = builder.build();

        Request request = new Request.Builder().url(url).post(multipartBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoaddingDismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LoaddingDismiss();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    int status = object.optInt("status");
                    String msg = object.optString("msg");
                    if (status == 1) {
                        imgUrl = object.optString("data");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(mContext).load(bitmap).into(mIvHeader);
                            }
                        });

                    } else {
                        Looper.prepare();
                        showToast(msg);
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void refresh(String imgUrl) {
        Log.d("imagUrl", imgUrl);
        LoaddingShow();
        RetrofitUtil.getInstance().setlogo(token, imgUrl, new Subscriber<BaseResponse<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LoaddingDismiss();
                if (e instanceof DataResultException) {
                    DataResultException resultException = (DataResultException) e;
                    Looper.prepare();
                    showToast( resultException.getMsg());
                    Looper.loop();
                }
            }

            @Override
            public void onNext(BaseResponse<String> baseResponse) {
                LoaddingDismiss();
                if ("1".equals(baseResponse.getStatus())) {
                    Looper.prepare();
                    showToast( baseResponse.getMsg());
                    Looper.loop();
                } else {
                    Looper.prepare();
                    showToast( baseResponse.getMsg());
                    Looper.loop();
                }
            }
        });
    }
}
