package com.feemoo.phoneArea;

import java.io.Serializable;

/**
 * 创建：yiang
 * <p>
 * 描述：
 */
public class AreaCodeModel implements Serializable {


    private String name;
    private String tel;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
