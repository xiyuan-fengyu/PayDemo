package com.xiyuan.pay.ali.request;

/**
 * Created by xiyuan_fengyu on 2016/8/29.
 */
public class AlipayQueryRequest extends AlipayRequest {

    public AlipayQueryRequest(String params) {
        this.apiUrl = "https://openapi.alipay.com/gateway.do";
        this.params = params;
    }

}
