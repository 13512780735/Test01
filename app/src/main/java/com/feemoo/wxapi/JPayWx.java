package com.feemoo.wxapi;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class JPayWx {
    private static JPayWx mJPay;
    private Context mContext;

    private JPayWx(Context context) {
        mContext = context;
    }

    public static JPayWx getIntance(Activity context) {
        if (mJPay == null) {
            synchronized (JPayWx.class) {
                if (mJPay == null) {
                    mJPay = new JPayWx(context.getApplicationContext());
                }
            }
        }
        return mJPay;
    }

    public interface WxPayListener {
        //支付成功
        void onPaySuccess();

        //支付失败
        void onPayError(int error_code, String message);

        //支付取消
        void onPayCancel();
    }

    public void toWxPay(String payParameters, WxPayListener listener) {
        if (payParameters != null) {
            JSONObject param = null;
            try {
                param = new JSONObject(payParameters);
            } catch (JSONException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            if (TextUtils.isEmpty(param.optString("appId")) || TextUtils.isEmpty(param.optString("partnerId"))
                    || TextUtils.isEmpty(param.optString("prepayId")) || TextUtils.isEmpty(param.optString("nonceStr"))
                    || TextUtils.isEmpty(param.optString("timeStamp")) || TextUtils.isEmpty(param.optString("sign"))) {
                if (listener != null) {
                    listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            toWxPay(param.optString("appId"),
                    param.optString("partnerId"), param.optString("prepayId"),
                    param.optString("nonceStr"), param.optString("timeStamp"),
                    param.optString("sign"),param.optString("package"), listener);

        } else {
            if (listener != null) {
                listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }

    public void toWxPay(String appId, String partnerId, String prepayId,
                        String nonceStr, String timeStamp, String sign,String packages, WxPayListener listener) {
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(partnerId)
                || TextUtils.isEmpty(prepayId) || TextUtils.isEmpty(nonceStr)
                || TextUtils.isEmpty(timeStamp) || TextUtils.isEmpty(sign)||TextUtils.isEmpty(packages)) {
            if (listener != null) {
                listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
            return;
        }
        WeiXinPay.getInstance(mContext).startWXPay(appId, partnerId, prepayId, nonceStr, timeStamp, sign,packages, listener);
    }
}