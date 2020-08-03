package com.fmapp.test01.activity.MyInfo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fmapp.test01.R;
import com.fmapp.test01.base.BaseActivity;
import com.fmapp.test01.utils.GlideEngine;
import com.fmapp.test01.utils.ImageUtils;
import com.fmapp.test01.widght.BorderTextView;
import com.fmapp.test01.widght.CircleImageView;
import com.fmapp.test01.widght.dialog.ActionSheetDialog;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MemberActivity extends BaseActivity {
    @BindView(R.id.ivHeader)
    CircleImageView mIvHeader;
    @BindView(R.id.mTvSave)
    BorderTextView mTvSave;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        initUI();
    }

    private List<String> mCloudData = new ArrayList<>();

    private void initUI() {

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
                GlideEngine.getInstance().loadGif(mContext, resultPhotos.get(0).uri, mIvHeader);
                return;
            }
        } else if (RESULT_CANCELED == resultCode) {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        }
    }
}
