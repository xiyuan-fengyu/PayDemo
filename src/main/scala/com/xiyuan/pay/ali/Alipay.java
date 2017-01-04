package com.xiyuan.pay.ali;

import com.xiyuan.pay.ali.request.AlipayQueryRequest;
import com.xiyuan.pay.ali.response.AlipayAsyncNotifyResult;
import com.xiyuan.pay.ali.response.AlipayQueryResult;
import com.xiyuan.pay.ali.value.AlipayStatus;
import com.xiyuan.pay.util.DateUtil;
import com.xiyuan.pay.util.RsaUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xiyuan_fengyu on 2016/8/25.
 */
public class Alipay {

    public static String generatePayInfo(String id, String goodsName, String describe, String orderId, double amount) {
        List<KeyVal> keyVals = getOrderKeyVals(id, goodsName, describe, orderId, amount);

        StringBuilder strBld = new StringBuilder();
        StringBuilder strEncodedBld = new StringBuilder();
        mergeToStr(keyVals, strBld, strEncodedBld);

        String sign = RsaUtils.rsaSign(strBld.toString(), AlipayCfg.private_key);
        if (sign == null) {
            return null;
        }
        KeyVal signKeyVal = new KeyVal("sign", sign);
        return strEncodedBld.toString() + "&sign=" + signKeyVal.valueEncoded;
    }

    private static void mergeToStr(List<KeyVal> keyVals, StringBuilder normalBld, StringBuilder encodedBld) {
        if (keyVals != null && keyVals.size() > 0) {
            for (int i = 0, last = keyVals.size() - 1; i < last; i++) {
                KeyVal keyVal = keyVals.get(i);
                if (normalBld != null) {
                    normalBld.append(keyVal.key).append('=').append(keyVal.value).append('&');
                }
                if (encodedBld != null) {
                    encodedBld.append(keyVal.key).append('=').append(keyVal.valueEncoded).append('&');
                }
            }

            {
                KeyVal keyVal = keyVals.get(keyVals.size() - 1);
                if (normalBld != null) {
                    normalBld.append(keyVal.key).append('=').append(keyVal.value);
                }
                if (encodedBld != null) {
                    encodedBld.append(keyVal.key).append('=').append(keyVal.valueEncoded);
                }
            }
        }
    }

    private static List<KeyVal> getOrderKeyVals(String id, String goodsName, String describe, String orderId, double amount) {
        List<KeyVal> keyVals = new ArrayList<>();
        keyVals.add(new KeyVal("app_id", "" + AlipayCfg.app_id));
        keyVals.add(new KeyVal("biz_content", "{" +
                "\"body\":\"" + describe + "\"," +
                "\"subject\":\"" + goodsName + "\"," +
                "\"out_trade_no\":\"" + orderId + "\"," +
                "\"seller_id\":\"" + AlipayCfg.seller_id + "\"," +
                "\"timeout_express\":\"" + AlipayCfg.timeout + "\"," +
                "\"total_amount\":\"" + (AlipayCfg.debug? 0.01: amount) + "\"," +
                "\"product_code\":\"QUICK_MSECURITY_PAY\"}"));
        keyVals.add(new KeyVal("charset", "utf-8"));
        keyVals.add(new KeyVal("format", "json"));
        keyVals.add(new KeyVal("method", "alipay.trade.app.pay"));
        keyVals.add(new KeyVal("notify_url", AlipayCfg.notify_url + "/" + id));
        keyVals.add(new KeyVal("sign_type", "RSA"));
        keyVals.add(new KeyVal("timestamp", DateUtil.nowToStr()));
        keyVals.add(new KeyVal("version", "1.0"));
        return keyVals;
    }

    /**
     * 验证异步通知是否真实
     * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.I2YTLn&treeId=204&articleId=105301&docType=1
     * @param result
     * @param out_trade_no
     * @param total_amount
     * @return
     */
    public static boolean checkAsyncNotifyResult(AlipayAsyncNotifyResult result, String out_trade_no, double total_amount) {
        if (result == null) {
            return false;
        }

        //构造按键字典升序排列的键值对List
        List<KeyVal> keyVals = asyncNotifyResultToKeyVals(result);

        //构建参数字符串
        StringBuilder strBld = new StringBuilder();
        mergeToStr(keyVals, strBld, null);

        //用支付宝公钥做校验
        if (!RsaUtils.rsaCheck(strBld.toString(), result.getSign(), AlipayCfg.alipay_public_key)) {
            return false;
        }

        //验证订单的四个重要信息是否匹配
        return result.getOut_trade_no().equals(out_trade_no) && result.getTotal_amount() == total_amount && result.getSeller_id().equals("" + AlipayCfg.seller_id) && result.getApp_id().equals("" + AlipayCfg.app_id);
    }

    /**
     * 将 AlipayAsyncNotifyResult 转换为已排序的 KeyVal list
     * @param result
     * @return
     */
    private static List<KeyVal> asyncNotifyResultToKeyVals(AlipayAsyncNotifyResult result) {
        ArrayList<KeyVal> keyVals = new ArrayList<>();
        if (result != null) {
            Field[] fields = AlipayAsyncNotifyResult.class.getDeclaredFields();
            //获取所有值不为空的字段
            for (Field field: fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(result);
                    String fieldName = field.getName();
                    if (value != null && !fieldName.matches("sign_type|sign")) {
                        keyVals.add(new KeyVal(field.getName(), value.toString(), false));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            //按字段名字典升序排序
            Collections.sort(keyVals, new Comparator<KeyVal>() {
                @Override
                public int compare(KeyVal o1, KeyVal o2) {
                    return o1.key.compareTo(o2.key);
                }
            });
        }
        return keyVals;
    }

    public static AlipayQueryResult queryPayStatus(String out_trade_no) {
        List<KeyVal> keyVals = new ArrayList<>();
        keyVals.add(new KeyVal("app_id", "" + AlipayCfg.app_id));
        keyVals.add(new KeyVal("biz_content", "{" +
                "\"out_trade_no\":\"" + out_trade_no + "\"" +
                "}"
        ));
        keyVals.add(new KeyVal("charset", "utf-8"));
        keyVals.add(new KeyVal("format", "json"));
        keyVals.add(new KeyVal("method", "alipay.trade.query"));
        keyVals.add(new KeyVal("sign_type", "RSA"));
        keyVals.add(new KeyVal("timestamp", DateUtil.nowToStr()));
        keyVals.add(new KeyVal("version", "1.0"));

        StringBuilder strBld = new StringBuilder();
        StringBuilder strEncodedBld = new StringBuilder();
        mergeToStr(keyVals, strBld, strEncodedBld);

        String sign = RsaUtils.rsaSign(strBld.toString(), AlipayCfg.private_key);
        if (sign == null) {
            return null;
        }
        KeyVal signKeyVal = new KeyVal("sign", sign);
        String params = strEncodedBld.toString() + "&sign=" + signKeyVal.valueEncoded;
        return new AlipayQueryRequest(params).execute(AlipayQueryResult.class);
    }

    public static boolean isPaySuccessOrFinish(AlipayQueryResult payResult) {
        if (payResult == null) {
            return false;
        }
        String tradeStatus = payResult.getAlipay_trade_query_response().getTrade_status();
        return tradeStatus.equals(AlipayStatus.TRADE_SUCCESS) || tradeStatus.equals(AlipayStatus.TRADE_FINISHED);
    }

    private static class KeyVal {

        private final String key;

        private final String value;

        private final String valueEncoded;

        public KeyVal(String key, String value) {
            this(key, value, true);
        }

        public KeyVal(String key, String value, boolean shouldEncode) {
            this.key = key;
            this.value = value;

            if (shouldEncode) {
                String tempVal = null;
                try {
                    tempVal = URLEncoder.encode(value, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    tempVal = "";
                }
                this.valueEncoded = tempVal;
            }
            else {
                this.valueEncoded = null;
            }
        }

    }

}
