package com.fmapp.test01.network.util;

import java.io.IOException;

public class DataResultException extends IOException {
    private String msg;
    private int status;

    public DataResultException(String msg, int status) {
        this.msg = msg;
        this.status = status;
    }

    public DataResultException(String message, String msg, int status) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
