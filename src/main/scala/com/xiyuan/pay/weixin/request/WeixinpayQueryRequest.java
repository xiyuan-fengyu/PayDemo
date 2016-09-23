package com.xiyuan.pay.weixin.request;

import com.xiyuan.pay.util.DateUtil;
import com.xiyuan.pay.util.RandomUtil;
import com.xiyuan.pay.weixin.WeixinpayCfg;
import com.xiyuan.pay.weixin.util.SignUtil;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xiyuan_fengyu on 2016/8/26.
 */
public class WeixinpayQueryRequest extends WeixinpayRequest {

    public final String appid;

    public final String mch_id;

    public final String out_trade_no;

    public final String nonce_str;

    public final String sign;

    public WeixinpayQueryRequest(String out_trade_no) {
        this.apiUrl = "https://api.mch.weixin.qq.com/pay/orderquery";

        this.appid = WeixinpayCfg.app_id;
        this.mch_id = "" + WeixinpayCfg.mch_id;
        this.nonce_str = RandomUtil.generateStr(10, 20);
        this.out_trade_no = out_trade_no;

        Map<String, String> params = new TreeMap<>();
        params.put("appid", this.appid);
        params.put("mch_id", this.mch_id);
        params.put("nonce_str", this.nonce_str);
        params.put("out_trade_no", this.out_trade_no);

        this.sign = SignUtil.sign(params, WeixinpayCfg.mch_key);
        params.put("sign", this.sign);

        this.params = params;
    }

}
