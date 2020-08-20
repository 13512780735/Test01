package com.feemoo.fmapp.network.model;

public class ImageModel {

    /**
     * msg : 上传文件扩展名是不允许的扩展名。
     只允许gif,jpg,jpeg,ico,png,bmp格式。
     * status : 0
     * data :
     */

    private String msg;
    private int status;
    private String data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
