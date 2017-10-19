package com.xiyuan.payV2.weixin.model;

import com.xiyuan.payV2.util.RandomUtil;
import com.xiyuan.payV2.weixin.WeixinpayCfg;
import com.xiyuan.payV2.weixin.util.SignUtil;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xiyuan_fengyu on 2017/09/09.
 */
public class PayReq {

	public static Map<String, String> create(String prepayId) {
		TreeMap<String, String> params = new TreeMap<>();
		params.put("appid", WeixinpayCfg.app_id);
		params.put("partnerid", "" + WeixinpayCfg.merchant_id);
		params.put("prepayid", prepayId);
		params.put("package", "Sign=WXPay");
		params.put("noncestr", RandomUtil.generateStr(20));
		params.put("timestamp", "" + System.currentTimeMillis() / 1000);

		Map<String, String> keyChangedParams = new TreeMap<>();
		keyChangedParams.put("appId", params.get("appid"));
		keyChangedParams.put("partnerId", params.get("partnerid"));
		keyChangedParams.put("prepayId", params.get("prepayid"));
		keyChangedParams.put("packageValue", params.get("package"));
		keyChangedParams.put("nonceStr", params.get("noncestr"));
		keyChangedParams.put("timeStamp", params.get("timestamp"));
		keyChangedParams.put("sign", SignUtil.sign(params, WeixinpayCfg.merchant_key));
		return keyChangedParams;
	}

}