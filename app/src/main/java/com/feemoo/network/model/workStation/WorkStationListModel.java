package com.feemoo.network.model.workStation;

import java.util.List;

public class WorkStationListModel {

    /**
     * folder : [{"id":"95794","name":"AZNQ0825.mp4","intime":"2020.08.03 09:20:57"}]
     * files : [{"id":"1029786","name":"AZNQ0825.mp4.zip","ext":"zip","size":"51.12 M","intime":"2020.08.03 09:19:44","basename":"AZNQ0825.mp4","basesize":"53607899"},{"id":"1030371","name":"FeiMaoJiangHu.ipa","ext":"ipa","size":"8.17 M","intime":"2020.08.03 09:18:47","basename":"FeiMaoJiangHu","basesize":"8570305"}]
     * space : {"used":"10.62 M","total":"2G"}
     */

    private SpaceBean space;
    private List<FolderBean> folder;
    private List<FilesBean> files;

    public SpaceBean getSpace() {
        return space;
    }

    public void setSpace(SpaceBean space) {
        this.space = space;
    }

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

    public static class SpaceBean {
        /**
         * used : 10.62 M
         * total : 2G
         */

        private String used;
        private String total;

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }

    public static class FolderBean {
        /**
         * id : 95794
         * name : AZNQ0825.mp4
         * intime : 2020.08.03 09:20:57
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
         * id : 1029786
         * name : AZNQ0825.mp4.zip
         * ext : zip
         * size : 51.12 M
         * intime : 2020.08.03 09:19:44
         * basename : AZNQ0825.mp4
         * basesize : 53607899
         */

        private String id;
        private String name;
        private String ext;
        private String size;
        private String intime;
        private String basename;
        private String basesize;

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

        public String getBasename() {
            return basename;
        }

        public void setBasename(String basename) {
            this.basename = basename;
        }

        public String getBasesize() {
            return basesize;
        }

        public void setBasesize(String basesize) {
            this.basesize = basesize;
        }
    }
}
