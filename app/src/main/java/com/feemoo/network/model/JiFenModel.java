package com.feemoo.network.model;

import java.util.List;

public class JiFenModel {

    /**
     * sdown : 500
     * osvip : 50
     * tasks : [{"id":"91","tid":"4","tstatus":"2"},{"id":"92","tid":"5","tstatus":"0"},{"id":"93","tid":"6","tstatus":"0"},{"id":"94","tid":"7","tstatus":"0"},{"id":"95","tid":"8","tstatus":"0"},{"id":"96","tid":"9","tstatus":"1"}]
     * loop : 0
     * sigcount : 0
     * ifsign : 0
     * point : 20
     */

    private int sdown;
    private int osvip;
    private int loop;
    private int sigcount;
    private int ifsign;
    private int point;
    private List<TasksBean> tasks;

    public int getSdown() {
        return sdown;
    }

    public void setSdown(int sdown) {
        this.sdown = sdown;
    }

    public int getOsvip() {
        return osvip;
    }

    public void setOsvip(int osvip) {
        this.osvip = osvip;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public int getSigcount() {
        return sigcount;
    }

    public void setSigcount(int sigcount) {
        this.sigcount = sigcount;
    }

    public int getIfsign() {
        return ifsign;
    }

    public void setIfsign(int ifsign) {
        this.ifsign = ifsign;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public List<TasksBean> getTasks() {
        return tasks;
    }

    public void setTasks(List<TasksBean> tasks) {
        this.tasks = tasks;
    }

    public static class TasksBean {
        /**
         * id : 91
         * tid : 4
         * tstatus : 2
         */

        private String id;
        private String tid;
        private String tstatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getTstatus() {
            return tstatus;
        }

        public void setTstatus(String tstatus) {
            this.tstatus = tstatus;
        }
    }
}
