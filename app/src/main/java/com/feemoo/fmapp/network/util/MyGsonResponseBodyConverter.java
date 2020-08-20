package com.feemoo.fmapp.network.util;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class MyGsonResponseBodyConverter <T> implements Converter<ResponseBody,T> {
    private Gson gson;
    private Type type;

    public MyGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            BaseBean baseBean = gson.fromJson(response,BaseBean.class);
            if ("1".equals(baseBean.getStatus())) {
                return gson.fromJson(response,type);
            }
            throw new DataResultException(baseBean.getMsg(),baseBean.getStatus());
        }finally {
            value.close();
        }
    }
}
