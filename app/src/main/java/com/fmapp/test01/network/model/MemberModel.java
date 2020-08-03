package com.fmapp.test01.network.model;

public class MemberModel {

    /**
     * svipid : 1004
     * uexp : 15
     * svipexp : 60
     * username : 13548635452
     * logo : https://ucgimg.fmapp.com//img_server/kind/attached/thumb/20200730/20200730173030_34054.jpg
     * elevel : {"lv1":0,"lv2":0,"lv3":0,"lv4":1}
     * msgcount : 0
     * issvip : 1
     * endtime : 终身会员
     * slevel : SVIP1
     * isbindphone : 1
     */

    private String svipid;
    private String uexp;
    private String svipexp;
    private String username;
    private String logo;
    private ElevelBean elevel;
    private int msgcount;
    private int issvip;
    private String endtime;
    private String slevel;
    private int isbindphone;

    public String getSvipid() {
        return svipid;
    }

    public void setSvipid(String svipid) {
        this.svipid = svipid;
    }

    public String getUexp() {
        return uexp;
    }

    public void setUexp(String uexp) {
        this.uexp = uexp;
    }

    public String getSvipexp() {
        return svipexp;
    }

    public void setSvipexp(String svipexp) {
        this.svipexp = svipexp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ElevelBean getElevel() {
        return elevel;
    }

    public void setElevel(ElevelBean elevel) {
        this.elevel = elevel;
    }

    public int getMsgcount() {
        return msgcount;
    }

    public void setMsgcount(int msgcount) {
        this.msgcount = msgcount;
    }

    public int getIssvip() {
        return issvip;
    }

    public void setIssvip(int issvip) {
        this.issvip = issvip;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getSlevel() {
        return slevel;
    }

    public void setSlevel(String slevel) {
        this.slevel = slevel;
    }

    public int getIsbindphone() {
        return isbindphone;
    }

    public void setIsbindphone(int isbindphone) {
        this.isbindphone = isbindphone;
    }

    public static class ElevelBean {
        /**
         * lv1 : 0
         * lv2 : 0
         * lv3 : 0
         * lv4 : 1
         */

        private int lv1;
        private int lv2;
        private int lv3;
        private int lv4;

        public int getLv1() {
            return lv1;
        }

        public void setLv1(int lv1) {
            this.lv1 = lv1;
        }

        public int getLv2() {
            return lv2;
        }

        public void setLv2(int lv2) {
            this.lv2 = lv2;
        }

        public int getLv3() {
            return lv3;
        }

        public void setLv3(int lv3) {
            this.lv3 = lv3;
        }

        public int getLv4() {
            return lv4;
        }

        public void setLv4(int lv4) {
            this.lv4 = lv4;
        }
    }
}
