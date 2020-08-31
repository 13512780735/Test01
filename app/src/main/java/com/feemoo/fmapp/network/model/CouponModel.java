package com.feemoo.fmapp.network.model;

import java.util.List;

public class CouponModel {

    private List<CouponBean> coupon;

    public List<CouponBean> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<CouponBean> coupon) {
        this.coupon = coupon;
    }

    public static class CouponBean {
        /**
         * name : 单次SVIP下载券
         * ctype : 1
         * otime : 2025-12-10 06:59:26
         */

        private String name;
        private String ctype;
        private String otime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCtype() {
            return ctype;
        }

        public void setCtype(String ctype) {
            this.ctype = ctype;
        }

        public String getOtime() {
            return otime;
        }

        public void setOtime(String otime) {
            this.otime = otime;
        }
    }
}
