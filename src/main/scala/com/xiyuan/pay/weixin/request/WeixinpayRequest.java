package com.xiyuan.pay.weixin.request;

import com.xiyuan.pay.util.ClassUtil;
import com.xiyuan.pay.weixin.util.MapUtil;
import com.xiyuan.pay.weixin.util.XmlUtil;
import com.xiyuan.template.log.XYLog;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiyuan_fengyu on 2016/8/26.
 */
public class WeixinpayRequest {

    protected String apiUrl;

    protected Map<String, String> params;

    protected Map<String, String> execute(String urlStr, Map<String, String> params) {
        Map<String, String> result = new HashMap<>();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");

            //构建xml请求数据
            StringBuilder strBld = new StringBuilder();
            strBld.append("<xml>\n");
            Set<Map.Entry<String, String>> keyVals = params.entrySet();
            for (Map.Entry<String, String> keyVal: keyVals) {
                String key = keyVal.getKey();
                strBld.append("\t<").append(key).append("><![CDATA[").append(keyVal.getValue()).append("]]></").append(key).append(">\n");
            }
            strBld.append("</xml>");

            //connection.connect();通过下面的方式会隐式调用connect
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(strBld.toString().getBytes("utf-8"));
            out.flush();
            out.close();

            InputStream in = connection.getInputStream();
            XmlUtil.writeToMap(in, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> void execute(T t) {
        if (t == null) {
            return;
        }

        Map<String, String> result = execute(apiUrl, params);
        MapUtil.mapToObj(result, t);
    }

    public <T> T execute(Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            execute(t);
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
