package com.feemoo.fmapp.network.util;

import java.io.IOException;

public class DataResultException extends IOException {
    private String msg;
    private String status;

    public DataResultException(String msg, String status) {
        this.msg = msg;
        this.status = status;
    }

    public DataResultException(String message, String msg, String status) {
        super(message);
        this.msg = msg;
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
