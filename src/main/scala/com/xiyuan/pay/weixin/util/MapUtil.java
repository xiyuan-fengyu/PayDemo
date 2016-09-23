package com.xiyuan.pay.weixin.util;

import com.xiyuan.pay.util.ClassUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by xiyuan_fengyu on 2016/9/22.
 */
public class MapUtil {

    public static <T> void mapToObj(Map<String, String> map, T t) {
        if (map == null || map.isEmpty() || t == null) {
            return;
        }

        Class<?> clazz = t.getClass();
        try {
            Set<Map.Entry<String, String>> keyVals = map.entrySet();
            for (Map.Entry<String, String> keyVal: keyVals) {
                ClassUtil.setValue(clazz, t, keyVal.getKey(), keyVal.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
