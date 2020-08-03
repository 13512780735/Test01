package com.fmapp.test01.utils;

import com.fmapp.test01.R;

public class com {
    public static int GetHeaderImgById(String ext) {

        if ("zip".equals(ext)) {
            return R.mipmap.icon_zip;
        } else if ("png".equals(ext)) {
            return R.mipmap.icon_photo;
        } else if ("png".equals(ext)) {
            return R.mipmap.icon_photo;
        } else if ("ipa".equals(ext)) {
            return R.mipmap.icon_mac;
        } else if ("mp3".equals(ext)) {
            return R.mipmap.icon_music;
        } else if ("exe".equals(ext)) {
            return R.mipmap.icon_exe;
        } else if ("txt".equals(ext)) {
            return R.mipmap.icon_txt;
        } else if ("doc".equals(ext)) {
            return R.mipmap.icon_word;
        } else if ("ppt".equals(ext)) {
            return R.mipmap.icon_ppt;
        } else if ("xls".equals(ext)) {
            return R.mipmap.icon_excelicon;
        } else if ("mp4".equals(ext)) {
            return R.mipmap.icon_video;
        } else if ("common".equals(ext)) {
            return R.mipmap.icon_general;
        } else if ("apk".equals(ext)) {
            return R.mipmap.icon_apki;
        } else
            return R.mipmap.icon_folder;
    }
}
