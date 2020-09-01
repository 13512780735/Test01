package com.feemoo.network.model.star;

import java.util.List;

public class StarListModel {

    private List<FilesBean> files;

    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    public static class FilesBean {
        /**
         * name : IMG_0365.mov.zip
         * id : 3940720
         * fid : 1299241
         * in_time : 1596420456
         * ext : zip
         * size : 10.23 M
         * intime : 2020.07.27 08:53:49
         * fshort : dfui8dmy
         * basename : IMG_0365.mov
         */

        private String name;
        private String id;
        private String fid;
        private String in_time;
        private String ext;
        private String size;
        private String intime;
        private String fshort;
        private String basename;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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

        public String getIn_time() {
            return in_time;
        }

        public void setIn_time(String in_time) {
            this.in_time = in_time;
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

        public String getIntime() {
            return intime;
        }

        public void setIntime(String intime) {
            this.intime = intime;
        }

        public String getFshort() {
            return fshort;
        }

        public void setFshort(String fshort) {
            this.fshort = fshort;
        }

        public String getBasename() {
            return basename;
        }

        public void setBasename(String basename) {
            this.basename = basename;
        }
    }
}
