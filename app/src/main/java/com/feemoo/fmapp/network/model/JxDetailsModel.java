package com.feemoo.fmapp.network.model;

import java.util.List;

public class JxDetailsModel {

    /**
     * id : 69823
     * ptype : 33
     * name : Excel设置下拉菜单，录入报表就这么简单
     * fshort : qs8bcirn
     * extension :
     * size : 0.00 B
     * time : 25天前
     * cover : https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200730/20200730163025_43927.jpg
     * basesize : 0
     * face : https://ucgimg.fmapp.com//Public/imgquality/attached/thumb/20200614/20200614223104_39228.jpg
     * username : c****l
     * link : https://www.feimaoyun.com/#/jx/qs8bcirn
     * ext : common
     * st1 :
     * st2 :
     * vhtml : <p><span style="font-size: large;">当需要在表格里录入数据时，大部分人采用的是手动录入，这种方法不仅麻烦，效率还很低，所以下面就介绍一种选择项录入方法，在录入一些固定内容时，直接使用事先设置好的下拉菜单就能简单录入，让表格录入变得更加简单快捷。</span><br></p><p><span style="font-size: large;"><br></span></p><p><span style="font-size: large; font-weight: bold;">一、设置下拉菜单</span></p><p><span style="font-size: large;">在表格中，设置下拉菜单的单元格，在<span style="color: rgb(238, 35, 13); font-weight: bold;">[数据]</span>菜单中，选择<span style="font-weight: bold; color: rgb(238, 35, 13);">[有效性]</span>，在弹出的<span style="font-weight: bold; color: rgb(238, 35, 13);">[数据有效性]</span>对话框内，将<span style="font-weight: bold; color: rgb(238, 35, 13);">[允许]</span>改为<span style="font-weight: bold; color: rgb(238, 35, 13);">[序列]</span>，<span style="font-weight: bold; color: rgb(238, 35, 13);">[来源]</span>改为添加的数据，这里以<span style="font-weight: bold; color: rgb(238, 35, 13);">[一部,二部,三部,]</span>为例。可手动添加，也可以已添加好的数据。<br></span></p><p><img src="https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200730/20200730163211_49108.jpg" style="max-width: 100%; margin-right: auto; margin-left: auto; display: block;"></p><p><span style="font-size: large;">注意：手动输入时，数据中的逗号以英文状态下输入。之后点击[确定]即可。</span><br></p><p><img src="https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200730/20200730163241_99178.jpg" style="max-width: 100%; margin-right: auto; margin-left: auto; display: block;"></p><p><br></p><p><span style="font-size: large; font-weight: bold;">二、插入下拉列表</span></p>
     * imgs : ["https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200730/20200730163025_43927.jpg"]
     * bsfiles : [{"id":"71402","img":"https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200821/20200821100659_11176.png","name":"优惠券使用操作方法"},{"id":"71350","img":"https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200819/20200819105119_38173.png","name":"Window10永久激活教程"},{"id":"71121","img":"https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200813/20200813231720_76268.png","name":"PS教程  制作一把打开漂亮红色雨伞图片"},{"id":"71120","img":"https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200813/20200813200602_67823.jpg","name":"PS教程  人物头像照片超逼真磨皮方法"},{"id":"71081","img":"https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200813/20200813091253_78341.png","name":"飞猫云8.12云空间更新内容说明"},{"id":"71016","img":"https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200812/20200812094731_43812.jpg","name":"提取线稿的几种常用办法"},{"id":"70151","img":"https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200803/20200803152321_96309.jpg","name":"照片合成，用PS后期合成通话故事中的魔法悬浮场景"},{"id":"69823","img":"https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200730/20200730163025_43927.jpg","name":"Excel设置下拉菜单，录入报表就这么简单"}]
     */

    private String id;
    private String ptype;
    private String name;
    private String fshort;
    private String extension;
    private String size;
    private String time;
    private String cover;
    private String basesize;
    private String face;
    private String username;
    private String link;
    private String ext;
    private String st1;
    private String st2;
    private String vhtml;
    private List<String> imgs;
    private List<BsfilesBean> bsfiles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFshort() {
        return fshort;
    }

    public void setFshort(String fshort) {
        this.fshort = fshort;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBasesize() {
        return basesize;
    }

    public void setBasesize(String basesize) {
        this.basesize = basesize;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getSt1() {
        return st1;
    }

    public void setSt1(String st1) {
        this.st1 = st1;
    }

    public String getSt2() {
        return st2;
    }

    public void setSt2(String st2) {
        this.st2 = st2;
    }

    public String getVhtml() {
        return vhtml;
    }

    public void setVhtml(String vhtml) {
        this.vhtml = vhtml;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public List<BsfilesBean> getBsfiles() {
        return bsfiles;
    }

    public void setBsfiles(List<BsfilesBean> bsfiles) {
        this.bsfiles = bsfiles;
    }

    public static class BsfilesBean {
        /**
         * id : 71402
         * img : https://ucgimg.fmapp.com/Public/imgquality/attached/image/20200821/20200821100659_11176.png
         * name : 优惠券使用操作方法
         */

        private String id;
        private String img;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
