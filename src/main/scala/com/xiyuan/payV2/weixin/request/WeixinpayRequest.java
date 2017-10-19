package com.xiyuan.payV2.weixin.request;

import com.xiyuan.payV2.weixin.WeixinpayCfg;
import com.xiyuan.payV2.weixin.util.XmlUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by xiyuan_fengyu on 2017/09/09.
 */
public abstract class WeixinpayRequest {

    String apiUrl;

    protected TreeMap<String, String> params = new TreeMap<>();

    public Map<String, String> execute() {
        String mch_id = "" + WeixinpayCfg.merchant_id;

        try {
            //注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            char[] password = mch_id.toCharArray();
            try (FileInputStream in = new FileInputStream(new File(WeixinpayCfg.ssl_path))) {
                keyStore.load(in, password);//密码默认 merchant_id
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            SSLContext sslcontext = SSLContexts.custom() .loadKeyMaterial(keyStore, password).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER
            );
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
            HttpPost httpPost = new HttpPost(apiUrl); // 设置响应头信息
            httpPost.addHeader("Connection", "keep-alive");
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.addHeader("Host", "api.mch.weixin.qq.com");
            httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpPost.addHeader("Cache-Control", "max-age=0");
            httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");

            // 构建xml请求数据
            StringBuilder strBld = new StringBuilder();
            strBld.append("<xml>\n");
            Set<Map.Entry<String, String>> keyVals = params.entrySet();
            for (Map.Entry<String, String> keyVal : keyVals) {
                String key = keyVal.getKey();
                strBld.append("\t<").append(key).append("><![CDATA[").append(keyVal.getValue()).append("]]></")
                        .append(key).append(">\n");
            }
            strBld.append("</xml>");

            httpPost.setEntity(new StringEntity(strBld.toString(), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return XmlUtil.writeToMap(entity.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

}
