package com.fmapp.test01.download.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import androidx.core.content.FileProvider;

import com.SuperKotlin.pictureviewer.ImagePagerActivity;
import com.SuperKotlin.pictureviewer.PictureConfig;
import com.fmapp.test01.MyApplication;
import com.fmapp.test01.activity.file.OfficeActivity;
import com.fmapp.test01.activity.file.PictureActivity;
import com.fmapp.test01.activity.file.VideoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 4ndroidev on 16/10/13.
 */
public class FileManager {
    private static Intent intent;
    private Context context;

    public FileManager(Context context) {
        this.context = context;
    }

    public String getExtension(String path) {
        int index = path.lastIndexOf(".");
        if (index == -1 || index == path.length() - 1) return "unknown";
        return path.substring(index + 1).toLowerCase();
    }

    public static Intent openFile(Context mContext, String filePath, String name) {
        File file = new File(filePath);
        if (!file.exists()) return null;
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        Log.d("后缀名1", end);
        Log.d("后缀名2", filePath);
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            //return getAudioFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("3gp") || end.equals("mp4") || end.equals("mov") || end.equals("rmvb") || end.equals("AVI")) {
            return getVideoFileIntent(mContext, filePath,name);
           // return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(mContext, filePath, name);
        } else if (end.equals("zip")) {
           // return getZipFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("rar")) {
            //return getRarFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("apk")) {
          //  return getApkFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("ppt") || end.equals("pptx")) {
            //return getPptFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("pptx")) {
           // return getPptxFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("xls")) {
           // return getExcelFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("xlsx")) {
           // return getExcelxFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("doc")) {
           // return getWordFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("docx")) {
            //return getWordFileIntent(mContext,filePath);
            return getWordx1FileIntent(mContext, filePath,name);

        } else if (end.equals("pdf")) {
           // return getPdfFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("chm")) {
          //  return getChmFileIntent(mContext, filePath);
            return getWordx1FileIntent(mContext, filePath,name);
        } else if (end.equals("txt")) {
           // return getTextFileIntent(mContext, filePath, false);
            return getWordx1FileIntent(mContext, filePath,name);
        } else {
            return getAllIntent(mContext, filePath);
        }
    }

    //RAR
    private static Intent getRarFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/x-rar-compressed");
            mContext.startActivity(intent);
        } else {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/x-rar-compressed");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //zip
    private static Intent getZipFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/zip");
            mContext.startActivity(intent);
        } else {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/zip");
            mContext.startActivity(intent);
        }
        return intent;
    }


    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "*/*");
            mContext.startActivity(intent);
        } else {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "*/*");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        } else {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(Context mContext, String param, String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(mContext, VideoActivity.class);
            intent.putExtra("uri", param);
            intent.putExtra("name", name);
           // Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
           // intent.setDataAndType(uri, "video/*");
            mContext.startActivity(intent);
        } else {
//            intent = new Intent("android.intent.action.VIEW");
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.putExtra("oneshot", 0);
//            intent.putExtra("configchange", 0);
//            Uri uri = Uri.fromFile(new File(param));
//            intent.setDataAndType(uri, "video/*");
//            mContext.startActivity(intent);
            intent = new Intent(mContext, VideoActivity.class);
            intent.putExtra("uri", param);
            intent.putExtra("name", name);
            // Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            // intent.setDataAndType(uri, "video/*");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "audio/*");
            mContext.startActivity(intent);
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "audio/*");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(Context mContext, String param, String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent = new Intent("android.intent.action.VIEW");
//            intent.addCategory("android.intent.category.DEFAULT");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent = new Intent(mContext, PictureActivity.class);
            intent.putExtra("uri", param);
            intent.putExtra("name", name);
            intent.putExtra("flag","0");
            //intent .setType("image/*");
            mContext.startActivity(intent);


        } else {
            intent = new Intent(mContext, PictureActivity.class);
            intent.putExtra("uri", param);
            intent.putExtra("name", name);
            intent.putExtra("flag","0");
            //  intent.setDataAndType(uri, "image/*");
            mContext.startActivity(intent);
           // Uri uri = Uri.fromFile(new File(param));
//            List<String> llist = new ArrayList<>();
//            llist.add(param);
//            PictureConfig config = new PictureConfig.Builder()
//                    .setListData((ArrayList<String>) llist)//图片数据List<String> list
//                    .setPosition(0)//图片下标（从第position张图片开始浏览）
//                    .setDownloadPath("pictureviewer")//图片下载文件夹地址
//                    .setIsShowNumber(false)//是否显示数字下标
//                    .needDownload(false)//是否支持图片下载
//                    .build();
//            ImagePagerActivity.startActivity(mContext, config);

        }
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            mContext.startActivity(intent);
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //打开pptx
    private static Intent getPptxFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
            mContext.startActivity(intent);
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/vnd.ms-excel");
            mContext.startActivity(intent);
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/vnd.ms-excel");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //打开exclex
    public static Intent getExcelxFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            mContext.startActivity(intent);
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/msword");
            mContext.startActivity(intent);
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/msword");
            mContext.startActivity(intent);
        }
        return intent;
    }
    //打开docx
    public static Intent getWordx1FileIntent(Context mContext, String param, String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent(mContext, OfficeActivity.class);
          //  Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.putExtra("uri", param);
            intent.putExtra("name", name);
            mContext.startActivity(intent);
        } else {
            intent = new Intent(mContext, OfficeActivity.class);
           // Uri uri = Uri.fromFile(new File(param));
            intent.putExtra("uri", param);
            intent.putExtra("name", name);
            //  intent.setDataAndType(uri, "image/*");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //打开docx
    public static Intent getWordxFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            mContext.startActivity(intent);
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            mContext.startActivity(intent);
        }
        return intent;
    }


    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/x-chm");
            mContext.startActivity(intent);
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/x-chm");
            mContext.startActivity(intent);
        }
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(Context mContext, String param, boolean paramBoolean) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (paramBoolean) {
                Uri uri1 = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
                intent.setDataAndType(uri1, "text/plain");
            } else {
                Uri uri1 = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
                intent.setDataAndType(uri1, "text/plain");
            }
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/x-chm");
            mContext.startActivity(intent);
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (paramBoolean) {
                Uri uri1 = Uri.parse(param);
                intent.setDataAndType(uri1, "text/plain");
            } else {
                Uri uri2 = Uri.fromFile(new File(param));
                intent.setDataAndType(uri2, "text/plain");
            }
            mContext.startActivity(intent);
        }

        return intent;
    }

    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(Context mContext, String param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(mContext, "com.fmapp.test01.fileprovider", new File(param));
            intent.setDataAndType(uri, "application/pdf");
            mContext.startActivity(intent);
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/pdf");
            mContext.startActivity(intent);
        }
        return intent;
    }
}
