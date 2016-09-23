package com.xiyuan.pay.weixin;

import java.util.Properties;

import com.xiyuan.pay.util.ConfigUtil;

public class WeixinpayCfg {

	private static final Properties properties = ConfigUtil.loadProperties("pay/WeixinpayCfg.properties");

	public static final int mch_id = Integer.parseInt(properties.getProperty("mch.id"));

	public static final String app_id = properties.getProperty("app.id");

	public static final boolean debug = Boolean.parseBoolean(properties.getProperty("debug"));

	public static final String trade_type = properties.getProperty("trade.type");

	public static final int time_expire = Integer.parseInt(properties.getProperty("time.expire"));

	public static final String mch_key = properties.getProperty("mch.key");

	public static final String notify_url = properties.getProperty("notify.url");

}