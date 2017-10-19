package com.xiyuan.payV2.weixin.util;

import com.xiyuan.payV2.util.ClassUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiyuan_fengyu on 2016/9/22.
 */
public class XmlUtil {

	@SuppressWarnings("rawtypes")
	public static Map<String, String> writeToMap(InputStream in) {
		Map<String, String> map = new HashMap<>();
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(in);
			Element root = document.getRootElement();
			if (root.getName().equals("xml")) {
				List children = root.elements();
				for (Object child : children) {
					if (child instanceof Element) {
						Element childEle = (Element) child;
						map.put(childEle.getName(), childEle.getStringValue());
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static <T> T convertoObj(InputStream in, Class<T> clazz) {
		T t = null;
		try {
			t = clazz.newInstance();
			Map<String, String> map = writeToMap(in);

			if (map.isEmpty() || t == null) {
				return t;
			}

			ClassUtil.convert(map, t);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}

}
