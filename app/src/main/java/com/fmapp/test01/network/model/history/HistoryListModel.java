package com.fmapp.test01.network.model.history;

import java.util.List;

public class HistoryListModel {

    /**
     * files : [{"id":"3754779","fid":"13109472","intime":"2020.06.15 14:55:57","name":"归档.zip.zip","ext":"zip","size":"1.10 G"},{"id":"3754773","fid":"2713480","intime":"2020.04.10 11:22:13","name":"归档_2.zip","ext":"zip","size":"1.12 G"}]
     * issvip : 1
     */

    private int issvip;
    private List<FilesBean> files;

    public int getIssvip() {
        return issvip;
    }

    public void setIssvip(int issvip) {
        this.issvip = issvip;
    }

    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    public static class FilesBean {
        /**
         * id : 3754779
         * fid : 13109472
         * intime : 2020.06.15 14:55:57
         * name : 归档.zip.zip
         * ext : zip
         * size : 1.10 G
         */

        private String id;
        private String fid;
        private String intime;
        private String name;
        private String ext;
        private String size;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getIntime() {
            return intime;
        }

        public void setIntime(String intime) {
            this.intime = intime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
