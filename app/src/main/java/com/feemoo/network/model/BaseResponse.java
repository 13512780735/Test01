package com.feemoo.network.model;

import java.io.Serializable;

/**
 * 获取json数据基类
 *
 * @author nanchen
 * @fileName RetrofitRxDemoo
 * @packageName com.nanchen.retrofitrxdemoo
 * @date 2016/12/09  17:05
 * "isFalg": true
 */

public class BaseResponse<T> implements Serializable {
    public String status;
    public String msg;
    public T data;
    public boolean isFalg;

    public boolean isFalg() {
        return isFalg;
    }

    public void setFalg(boolean falg) {
        isFalg = falg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
