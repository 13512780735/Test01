package com.feemoo.network.model;

import java.util.List;

public class FilesListModel {

    private List<FolderBean> folder;
    private List<FilesBean> files;

    public List<FolderBean> getFolder() {
        return folder;
    }

    public void setFolder(List<FolderBean> folder) {
        this.folder = folder;
    }

    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    public static class FolderBean {
        /**
         * id : 51844
         * name : test
         * intime : 2020.07.30 16:53:40
         */

        private String id;
        private String name;
        private String intime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIntime() {
            return intime;
        }

        public void setIntime(String intime) {
            this.intime = intime;
        }
    }

    public static class FilesBean {
        /**
         * id : 3944752
         * name : 微信支付logo_1.75x.png
         * size : 5.81 K
         * ext : png
         * intime : 2020.07.30 16:56:01
         * basename : 微信支付logo_1.75x
         * link : http://www.fmpan.com/#/s/uj22b656
         */

        private String id;
        private String name;
        private String size;
        private String ext;
        private String intime;
        private String basename;
        private String link;
        private String isshare;

        public String getIsshare() {
            return isshare;
        }

        public void setIsshare(String isshare) {
            this.isshare = isshare;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public String getIntime() {
            return intime;
        }

        public void setIntime(String intime) {
            this.intime = intime;
        }

        public String getBasename() {
            return basename;
        }

        public void setBasename(String basename) {
            this.basename = basename;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
