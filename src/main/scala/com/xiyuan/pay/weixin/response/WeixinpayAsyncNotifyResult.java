package com.xiyuan.pay.weixin.response;

/**
 * Created by xiyuan_fengyu on 2016/9/22.
 *
 * https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_7&index=3
 */
public class WeixinpayAsyncNotifyResult {

    private String return_code;

    private String return_msg;

    private String appid;

    private String mch_id;

    private String device_info;

    private String nonce_str;

    private String sign;

    private String result_code;

    private String err_code;

    private String err_code_des;

    private String openid;

    private String is_subscribe;

    private String trade_type;

    private String bank_type;

    private Integer total_fee;

    private String fee_type;

    private Integer cash_fee;

    private String cash_fee_type;

    private Integer coupon_fee;

    private Integer coupon_count;

    private String coupon_id_0;

    private String coupon_id_1;

    private String coupon_id_2;

    private String coupon_id_3;

    private String coupon_id_4;

    private String coupon_id_5;

    private String coupon_id_6;

    private String coupon_id_7;

    private String coupon_id_8;

    private String coupon_id_9;

    private String coupon_fee_0;

    private String coupon_fee_1;

    private String coupon_fee_2;

    private String coupon_fee_3;

    private String coupon_fee_4;

    private String coupon_fee_5;

    private String coupon_fee_6;

    private String coupon_fee_7;

    private String coupon_fee_8;

    private String coupon_fee_9;

    private String transaction_id;

    private String out_trade_no;

    private String attach;

    private String time_end;

    public String getAppid() {
        return appid;
    }

    public String getAttach() {
        return attach;
    }

    public String getBank_type() {
        return bank_type;
    }

    public Integer getCash_fee() {
        return cash_fee;
    }

    public String getCash_fee_type() {
        return cash_fee_type;
    }

    public Integer getCoupon_count() {
        return coupon_count;
    }

    public Integer getCoupon_fee() {
        return coupon_fee;
    }

    public String getCoupon_fee_0() {
        return coupon_fee_0;
    }

    public String getCoupon_fee_1() {
        return coupon_fee_1;
    }

    public String getCoupon_fee_2() {
        return coupon_fee_2;
    }

    public String getCoupon_fee_3() {
        return coupon_fee_3;
    }

    public String getCoupon_fee_4() {
        return coupon_fee_4;
    }

    public String getCoupon_fee_5() {
        return coupon_fee_5;
    }

    public String getCoupon_fee_6() {
        return coupon_fee_6;
    }

    public String getCoupon_fee_7() {
        return coupon_fee_7;
    }

    public String getCoupon_fee_8() {
        return coupon_fee_8;
    }

    public String getCoupon_fee_9() {
        return coupon_fee_9;
    }

    public String getCoupon_id_0() {
        return coupon_id_0;
    }

    public String getCoupon_id_1() {
        return coupon_id_1;
    }

    public String getCoupon_id_2() {
        return coupon_id_2;
    }

    public String getCoupon_id_3() {
        return coupon_id_3;
    }

    public String getCoupon_id_4() {
        return coupon_id_4;
    }

    public String getCoupon_id_5() {
        return coupon_id_5;
    }

    public String getCoupon_id_6() {
        return coupon_id_6;
    }

    public String getCoupon_id_7() {
        return coupon_id_7;
    }

    public String getCoupon_id_8() {
        return coupon_id_8;
    }

    public String getCoupon_id_9() {
        return coupon_id_9;
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

    public String getFee_type() {
        return fee_type;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public String getMch_id() {
        return mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public String getOpenid() {
        return openid;
    }

    public String getOut_trade_no() {
        return out_trade_no;
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

    public String getTime_end() {
        return time_end;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }
}
