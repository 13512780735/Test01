package com.feemoo.fmapp.network.model;

import java.util.List;

public class VipModel {

    /**
     * svips : [{"vipid":1000,"price":"18","unit":"0"},{"vipid":1001,"price":"38","unit":"38"},{"vipid":1002,"price":"78","unit":"26"},{"vipid":1003,"price":"178","unit":"14"},{"vipid":1004,"price":"588","unit":"588"},{"vipid":1005,"price":"12","unit":"12"},{"vipid":1006,"price":"5","unit":"0"},{"vipid":1007,"price":"5","unit":"0"},{"vipid":1008,"price":"128","unit":"0"},{"vipid":1009,"price":"6","unit":"0"},{"vipid":1010,"price":"2.9","unit":"0"},{"vipid":1011,"price":"388","unit":"388"}]
     * user : {"issvip":"yes","uname":"13917167002","slevel":"SVIP6","ulogo":"https://ucgimg.fmapp.com//img_server/kind/attached/thumb/20200820/20200820144634_34317.png"}
     * coupon20 : {"id":"0"}
     * coupon50 : {"id":"0"}
     */

    private UserBean user;
    private Coupon20Bean coupon20;
    private Coupon50Bean coupon50;
    private List<SvipsBean> svips;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Coupon20Bean getCoupon20() {
        return coupon20;
    }

    public void setCoupon20(Coupon20Bean coupon20) {
        this.coupon20 = coupon20;
    }

    public Coupon50Bean getCoupon50() {
        return coupon50;
    }

    public void setCoupon50(Coupon50Bean coupon50) {
        this.coupon50 = coupon50;
    }

    public List<SvipsBean> getSvips() {
        return svips;
    }

    public void setSvips(List<SvipsBean> svips) {
        this.svips = svips;
    }

    public static class UserBean {
        /**
         * issvip : yes
         * uname : 13917167002
         * slevel : SVIP6
         * ulogo : https://ucgimg.fmapp.com//img_server/kind/attached/thumb/20200820/20200820144634_34317.png
         */

        private String issvip;
        private String uname;
        private String slevel;
        private String ulogo;

        public String getIssvip() {
            return issvip;
        }

        public void setIssvip(String issvip) {
            this.issvip = issvip;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getSlevel() {
            return slevel;
        }

        public void setSlevel(String slevel) {
            this.slevel = slevel;
        }

        public String getUlogo() {
            return ulogo;
        }

        public void setUlogo(String ulogo) {
            this.ulogo = ulogo;
        }
    }

    public static class Coupon20Bean {
        /**
         * id : 0
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class Coupon50Bean {
        /**
         * id : 0
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class SvipsBean implements Comparable<SvipsBean> {
        /**
         * vipid : 1000
         * price : 18
         * unit : 0
         */

        private int vipid;
        private String price;
        private String unit;
        private String oprice;
        private String name;
        private int img;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImg() {
            return img;
        }

        public void setImg(int img) {
            this.img = img;
        }

        public String getOprice() {
            return oprice;
        }

        public void setOprice(String oprice) {
            this.oprice = oprice;
        }

        public int getVipid() {
            return vipid;
        }

        public void setVipid(int vipid) {
            this.vipid = vipid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        @Override
        public int compareTo(SvipsBean svipsBean) {
            int i =  svipsBean.getVipid()-this.getVipid() ;//先按照年龄排序
            return i;
        }
    }
}
