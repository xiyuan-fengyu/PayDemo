package com.xiyuan.payV2.weixin.util;

import com.xiyuan.payV2.util.Md5Util;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xiyuan_fengyu on 2016/9/22.
 */
public class SignUtil {

	@SuppressWarnings("unchecked")
	public static String sign(Map<String, String> params, String key) {
		if (params == null) {
			return null;
		}

		TreeMap<String, String> treeMap = null;
		if (params instanceof TreeMap) {
			treeMap = (TreeMap<String, String>) params;
		}
		else {
			treeMap = new TreeMap<>();
			treeMap.putAll(params);
		}

		StringBuilder strBld = new StringBuilder();
		for (Map.Entry<String, String> keyVal : treeMap.entrySet()) {
			String aKey = keyVal.getKey();
			String aValue = keyVal.getValue();
			if (aValue != null && !"".equals(aValue) && !"sign".equals(aKey)) {
				strBld.append(aKey).append('=').append(aValue).append('&');
			}
		}
		strBld.append("key=").append(key);
		String md5 = Md5Util.get(strBld.toString(), StandardCharsets.UTF_8);
		return md5 == null ? null : md5.toUpperCase();
	}

	public static String sign(Object params, String key) {
		if (params == null) {
			return null;
		}

		TreeMap<String, String> treeMap = new TreeMap<>();
		Class<?> clazz = params.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			if (!"sign".equals(fieldName)) {
				try {
					Object fieldValue = field.get(params);
					if (fieldValue != null) {
						treeMap.put(fieldName, fieldValue.toString());
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return sign(treeMap, key);
	}

}
