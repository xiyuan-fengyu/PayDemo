package com.xiyuan.pay.weixin.model;

import com.xiyuan.pay.util.RandomUtil;
import com.xiyuan.pay.weixin.Weixinpay;
import com.xiyuan.pay.weixin.WeixinpayCfg;
import com.xiyuan.pay.weixin.annotation.SignProperty;
import com.xiyuan.pay.weixin.util.SignUtil;

/**
 * Created by xiyuan_fengyu on 2016/9/22.
 */
public class PayReq {

    @SignProperty(name = "appid")
    public final String appId;

    @SignProperty(name = "partnerid")
    public final String partnerId;

    @SignProperty(name = "prepayid")
    public final String prepayId;

    @SignProperty(name = "package")
    public final String packageValue;

    @SignProperty(name = "noncestr")
    public final String nonceStr;

    public final String sign;

    @SignProperty(name = "timestamp")
    public final String timeStamp;

    public PayReq(String prepayId) {
        this.appId = WeixinpayCfg.app_id;
        this.partnerId = "" + WeixinpayCfg.mch_id;
        this.prepayId = prepayId;
        this.packageValue = "Sign=WXPay";
        this.nonceStr = RandomUtil.generateStr(20);
        //单位为秒，否则无法支付成功
        this.timeStamp = "" + System.currentTimeMillis() / 1000;
        this.sign = SignUtil.sign(this, WeixinpayCfg.mch_key);
    }
}