package com.xiyuan.pay.weixin.request;

import com.xiyuan.pay.util.DateUtil;
import com.xiyuan.pay.util.Md5Util;
import com.xiyuan.pay.util.RandomUtil;
import com.xiyuan.pay.weixin.WeixinpayCfg;
import com.xiyuan.pay.weixin.util.SignUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xiyuan_fengyu on 2016/8/26.
 */
public class WeixinpayCreateRequest extends WeixinpayRequest {

    public final String appid;

    public final String mch_id;

    public final String nonce_str;

    public final String sign;

    public final String body;

    public final String detail;

    public final String out_trade_no;

    /**
     * 单位为分，必须为整数形式
     */
    public final String total_fee;

    public final String spbill_create_ip;

    public final String time_start;

    public final String time_expire;

    public final String notify_url;

    public final String trade_type;

    /**
     * @param  id 主键id
     * @param body 商品描述
     * @param detail 商品详情
     * @param out_trade_no 商户订单号
     * @param total_fee 总金额，单位元
     * @param spbill_create_ip 客户终端IP
     */
    public WeixinpayCreateRequest(String id, String body, String detail, String out_trade_no, double total_fee, String spbill_create_ip) {
        this.apiUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

        this.appid = WeixinpayCfg.app_id;
        this.body = body;
        this.detail = detail;
        this.mch_id = "" + WeixinpayCfg.mch_id;
        this.nonce_str = RandomUtil.generateStr(10, 20);
        this.notify_url = WeixinpayCfg.notify_url + "/"  + id;
        this.out_trade_no = out_trade_no;
        this.spbill_create_ip = spbill_create_ip;
        //配置中的单位为分钟
        this.time_expire = DateUtil.dateToNoDividerStr(new Date(System.currentTimeMillis() + WeixinpayCfg.time_expire * 60000));
        this.time_start = DateUtil.nowToNoDividerStr();
        //将单位转换为 分，并将结果转换为整数
        this.total_fee = WeixinpayCfg.debug? "1": (int) (total_fee * 100) + "";
        this.trade_type = WeixinpayCfg.trade_type;

        Map<String, String> params = new TreeMap<>();

        params.put("appid", this.appid);

        params.put("body", this.body);

        if (detail != null && !detail.isEmpty()) {
            params.put("detail", this.detail);
        }

        params.put("mch_id", this.mch_id);

        params.put("nonce_str", this.nonce_str);

        params.put("notify_url", this.notify_url);

        params.put("out_trade_no", this.out_trade_no);

        params.put("spbill_create_ip", this.spbill_create_ip);

        params.put("time_expire", this.time_expire);

        params.put("time_start", this.time_start);

        params.put("total_fee", this.total_fee);

        params.put("trade_type", this.trade_type);

        this.sign = SignUtil.sign(params, WeixinpayCfg.mch_key);
        params.put("sign", this.sign);

        this.params = params;
    }

}
