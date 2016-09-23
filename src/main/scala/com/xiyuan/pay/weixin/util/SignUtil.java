package com.xiyuan.pay.weixin.util;

import com.xiyuan.pay.util.Md5Util;
import com.xiyuan.pay.weixin.annotation.SignProperty;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xiyuan_fengyu on 2016/9/22.
 */
public class SignUtil {

    public static String sign(Object params, String key) {
        if (params == null) {
            return "";
        }

        TreeMap<String, String> treeMap = null;
        if (params instanceof  TreeMap) {
            treeMap = (TreeMap<String, String>) params;
        }
        else if (params instanceof Map) {
            treeMap = new TreeMap<>();
            treeMap.putAll((Map<String, String>) params);
        }
        else {
            treeMap = new TreeMap<>();

            Class<?> clazz = params.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field: fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (!"sign".equals(fieldName)) {
                    SignProperty signProperty = field.getAnnotation(SignProperty.class);
                    try {
                        Object fieldValue = field.get(params);
                        if (fieldValue != null) {
                            treeMap.put(signProperty == null? fieldName: signProperty.name(), fieldValue.toString());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return signOfTreeMap(treeMap, key);
    }

    private static String signOfTreeMap(TreeMap<String, String> treeMap, String key) {
        StringBuilder strBld = new StringBuilder();
        for (Map.Entry<String, String> keyVal: treeMap.entrySet()) {
            String aKey = keyVal.getKey();
            String aValue = keyVal.getValue();
            if (aValue != null && !"".equals(aValue) && !"sign".equals(aKey)) {
                strBld.append(aKey).append('=').append(aValue).append('&');
            }
        }
        strBld.append("key=").append(key);
        return Md5Util.get(strBld.toString(), "utf-8").toUpperCase();
    }

}
