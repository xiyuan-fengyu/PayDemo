package com.xiyuan.payV2.weixin.request;

import com.xiyuan.payV2.util.RandomUtil;
import com.xiyuan.payV2.weixin.WeixinpayCfg;
import com.xiyuan.payV2.weixin.util.SignUtil;

/**
 * Created by xiyuan_fengyu on 2017/09/09.
 */
public class WeixinpayRefundRequest extends WeixinpayRequest {

	/**
	 * @param outTradeNo 商户订单号
	 * @param outRefundNo 商户退款单号
	 * @param totalFee 订单总的金额
	 * @param refundFee 退款金额
	 */
	public WeixinpayRefundRequest(
			String outTradeNo,
			String outRefundNo,
			double totalFee,
			double refundFee
	) {
		this.apiUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";

		params.put("appid", WeixinpayCfg.app_id);
		params.put("mch_id", "" + WeixinpayCfg.merchant_id);
		params.put("nonce_str", RandomUtil.generateStr(10, 20));
		params.put("out_trade_no", outTradeNo);
		params.put("out_refund_no", outRefundNo);
		params.put("total_fee", WeixinpayCfg.debug ? "1" : (int) (totalFee * 100) + "");
		params.put("refund_fee", WeixinpayCfg.debug ? "1" : (int) (refundFee * 100) + "");
		params.put("sign", SignUtil.sign(params, WeixinpayCfg.merchant_key));
	}

}
