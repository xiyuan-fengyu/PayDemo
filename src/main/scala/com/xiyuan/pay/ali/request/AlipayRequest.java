package com.xiyuan.pay.ali.request;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiyuan_fengyu on 2016/8/29.
 */
public class AlipayRequest {

    protected String apiUrl;

    protected String params;

    protected String execute(String urlStr, String params) {
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

            //connection.connect();通过下面的方式会隐式调用connect
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(params.getBytes("utf-8"));
            out.flush();
            out.close();

            InputStream in = connection.getInputStream();
            String charset = getResponseCharset(connection.getContentType());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
            String line = null;
            StringBuilder strBld = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                strBld.append(line).append("\n");
            }
            in.close();
            reader.close();
            return strBld.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getResponseCharset(String ctype) {
        String charset = "UTF-8";
        if(ctype != null && !ctype.isEmpty()) {
            String[] params = ctype.split(";");
            String[] arr = params;
            int len = params.length;

            for(int i = 0; i < len; ++i) {
                String param = arr[i];
                param = param.trim();
                if(param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if(pair.length == 2 && pair[1] != null && !pair[1].isEmpty()) {
                        charset = pair[1].trim();
                    }
                    break;
                }
            }
        }

        return charset;
    }

    private static Gson gson = new Gson();

    public <T> T execute(Class<T> clazz) {
        String resultStr = execute(apiUrl, params);
        if (resultStr != null) {
            try {
                return gson.fromJson(resultStr, clazz);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
