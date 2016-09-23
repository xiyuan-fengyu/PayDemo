package com.xiyuan.pay.weixin.response;

/**
 * Created by xiyuan_fengyu on 2016/8/26.
 */
public class WeixinpayCreateResult {

    /**
     SUCCESS/FAIL
     此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
     */
    private String return_code;

    /**
     返回信息，如非空，为错误原因
     签名失败
     参数格式校验错误
     */
    private String return_msg;

    /**
     调用接口提交的应用ID
     */
    private String appid;

    /**
     调用接口提交的商户号
     */
    private String mch_id;

    /**
     调用接口提交的终端设备号，
     */
    private String device_info;

    /**
     微信返回的随机字符串
     */
    private String nonce_str;

    /**
     微信返回的签名
     详见签名算法   https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3
     */
    private String sign;

    /**
     业务结果 SUCCESS/FAIL
     */
    private String result_code;

    /**
     交易类型
     调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，详细说明见参数规定   https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_2
     */
    private String trade_type;

    /**
     预支付交易会话标识
     微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    private String prepay_id;

    /**
     错误代码
     */
    private String err_code;

    /**
     错误代码描述
     */
    private String err_code_des;


    public String getAppid() {
        return appid;
    }

    public String getDevice_info() {
        return device_info;
    }

    public String getErr_code() {
        return err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public String getMch_id() {
        return mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public String getResult_code() {
        return result_code;
    }

    public String getReturn_code() {
        return return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public String getSign() {
        return sign;
    }

    public String getTrade_type() {
        return trade_type;
    }
}
