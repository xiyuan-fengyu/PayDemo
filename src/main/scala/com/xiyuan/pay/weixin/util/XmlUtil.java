package com.xiyuan.pay.weixin.util;

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

    public static void writeToMap(InputStream in, Map<String, String> map) {
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(in);
            Element root = document.getRootElement();
            if (root.getName().equals("xml")) {
                List children = root.elements();
                for (Object child: children) {
                    if (child instanceof Element) {
                        Element childEle = (Element) child;
                        map.put(childEle.getName(), childEle.getStringValue());
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static <T> T convertoObj(InputStream in, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
            HashMap<String, String> map = new HashMap<>();
            writeToMap(in, map);
            MapUtil.mapToObj(map, t);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }


}
