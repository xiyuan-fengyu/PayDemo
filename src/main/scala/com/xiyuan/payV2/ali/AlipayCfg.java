package com.xiyuan.payV2.ali;

import java.util.Properties;

public class AlipayCfg {

	private static final Properties properties;

	static {
		properties = new Properties();
		try {
			properties.load(AlipayCfg.class.getClassLoader().getResourceAsStream("payV2/AlipayCfg.properties"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final boolean debug = Boolean.parseBoolean(properties.getProperty("debug"));

	public static final String data_format = properties.getProperty("data.format");

	public static final String notify_url = properties.getProperty("notify.url");

	public static final String data_sign_type = properties.getProperty("data.sign.type");

	public static final String alipay_public_key = properties.getProperty("alipay.public.key");

	public static final String merchant_private_key = properties.getProperty("merchant.private.key");

	public static final String timeout = properties.getProperty("timeout");

	public static final String disable_pay_channels = properties.getProperty("disable_pay_channels");

	public static final String data_charset = properties.getProperty("data.charset");

	public static final String gateway = properties.getProperty("gateway");

	public static final long app_id = Long.parseLong(properties.getProperty("app.id"));

	public static final long seller_id = Long.parseLong(properties.getProperty("seller.id"));

}