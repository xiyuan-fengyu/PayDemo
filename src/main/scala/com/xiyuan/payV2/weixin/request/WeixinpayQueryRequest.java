package com.xiyuan.payV2.weixin.request;

import com.xiyuan.payV2.util.RandomUtil;
import com.xiyuan.payV2.weixin.WeixinpayCfg;
import com.xiyuan.payV2.weixin.util.SignUtil;

import java.util.Date;

/**
 * Created by xiyuan_fengyu on 2017/09/09.
 */
public class WeixinpayQueryRequest extends WeixinpayRequest {

	/**
	 * @param outTradeNo 商户订单号
	 */
	public WeixinpayQueryRequest(
			String outTradeNo
	) {
		this.apiUrl = "https://api.mch.weixin.qq.com/pay/orderquery";

		Date now = new Date();

		params.put("appid", WeixinpayCfg.app_id);
		params.put("mch_id", "" + WeixinpayCfg.merchant_id);
		params.put("nonce_str", RandomUtil.generateStr(10, 20));
		params.put("out_trade_no", outTradeNo);
		params.put("sign", SignUtil.sign(params, WeixinpayCfg.merchant_key));
	}

}
