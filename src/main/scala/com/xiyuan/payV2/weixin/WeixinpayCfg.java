package com.xiyuan.payV2.weixin;

import java.util.Properties;

public class WeixinpayCfg {

	private static final Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(WeixinpayCfg.class.getClassLoader().getResourceAsStream("payV2/WeixinpayCfg.properties"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int merchant_id = Integer.parseInt(properties.getProperty("merchant.id"));

	public static final boolean debug = Boolean.parseBoolean(properties.getProperty("debug"));

	public static final String notify_url = properties.getProperty("notify.url");

	public static final String merchant_key = properties.getProperty("merchant.key");

	public static final String trade_type = properties.getProperty("trade.type");

	public static final String limit_pay = properties.getProperty("limit_pay");

	public static final int timeout = Integer.parseInt(properties.getProperty("timeout"));

	public static final String ssl_path = properties.getProperty("ssl.path");

	public static final String app_id = properties.getProperty("app.id");

}