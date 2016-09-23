package com.xiyuan.pay.ali;

import java.util.Properties;

import com.xiyuan.pay.util.ConfigUtil;

public class AlipayCfg {

	private static final Properties properties = ConfigUtil.loadProperties("pay/AlipayCfg.properties");

	public static final long app_id = Long.parseLong(properties.getProperty("app.id"));

	public static final boolean debug = Boolean.parseBoolean(properties.getProperty("debug"));

	public static final String private_key = properties.getProperty("private.key");

	public static final String alipay_public_key = properties.getProperty("alipay.public.key");

	public static final String timeout = properties.getProperty("timeout");

	public static final long seller_id = Long.parseLong(properties.getProperty("seller.id"));

	public static final String notify_url = properties.getProperty("notify.url");

}