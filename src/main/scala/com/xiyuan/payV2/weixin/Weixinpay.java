package com.xiyuan.payV2.weixin;

import com.xiyuan.payV2.weixin.model.PayReq;
import com.xiyuan.payV2.weixin.request.WeixinpayCreateRequest;
import com.xiyuan.payV2.weixin.request.WeixinpayQueryRequest;
import com.xiyuan.payV2.weixin.request.WeixinpayRefundRequest;
import com.xiyuan.payV2.weixin.util.SignUtil;
import com.xiyuan.payV2.weixin.value.WeixinpayStatus;

import java.util.Map;

public class Weixinpay {

    /**
     * @param outTradeNo 商户订单号
     * @param body 商品描述
     * @param detail 商品详情
     * @param totalFee 总金额，单位元
     * @param userIp 客户终端IP
     * @param otherParams 额外参数，用于替换 notify.url 中 {.*} 格式的参数值
     */
    public static Map<String, String> createNew(
            String outTradeNo,
            double totalFee,
            String body,
            String detail,
            String userIp,
            Map<String, String> otherParams
    ) {
        WeixinpayCreateRequest request = new WeixinpayCreateRequest(outTradeNo, body, detail, totalFee, userIp, otherParams);
        Map<String, String> response = request.execute();
        if (response.get("sign") != null && response.get("sign").equals(SignUtil.sign(response, WeixinpayCfg.merchant_key))
                && WeixinpayStatus.success.equals(response.get("return_code"))
                && WeixinpayStatus.success.equals(response.get("result_code"))) {
            return PayReq.create(response.get("prepay_id"));
        }
        return null;
    }

    public static Map<String, String> createWithPrepayId(String prepayId) {
        return PayReq.create(prepayId);
    }

    /**
     * @param outTradeNo 商户订单号
     * @param outRefundNo 商户退款单号
     * @param totalFee 订单总的金额，单位：元
     * @param refundFee 退款金额，单位：元
     */
    public static Map<String, String> refundResponse(
            String outTradeNo,
            String outRefundNo,
            double totalFee,
            double refundFee
    ) {
        WeixinpayRefundRequest request = new WeixinpayRefundRequest(outTradeNo, outRefundNo, totalFee, refundFee);
        return request.execute();
    }

    /**
     * @param outTradeNo 商户订单号
     * @param outRefundNo 商户退款单号
     * @param totalFee 订单总的金额，单位：元
     * @param refundFee 退款金额，单位：元
     */
    public static boolean refund(
            String outTradeNo,
            String outRefundNo,
            double totalFee,
            double refundFee
    ) {
        return refundSuccess(refundResponse(outTradeNo, outRefundNo, totalFee, refundFee));
    }

    public static boolean refundSuccess(Map<String, String> response) {
        if (response == null) return false;

        if (response.get("sign") != null && response.get("sign").equals(SignUtil.sign(response, WeixinpayCfg.merchant_key))
                && WeixinpayStatus.success.equals(response.get("return_code"))
                && WeixinpayStatus.success.equals(response.get("result_code"))) {
            return true;
        }
        return false;
    }

    public static Map<String, String> query(String outTradeNo) {
        WeixinpayQueryRequest request = new WeixinpayQueryRequest(outTradeNo);
        Map<String, String> response = request.execute();
        if (response.get("sign") != null && response.get("sign").equals(SignUtil.sign(response, WeixinpayCfg.merchant_key))
                && WeixinpayStatus.success.equals(response.get("return_code"))) {
            return response;
        }
        return null;
    }

    public static boolean isTradeFinishedOrSuccess(String outTradeNo) {
        return isTradeFinishedOrSuccess(query(outTradeNo));
    }

    public static boolean isTradeFinishedOrSuccess(Map<String, String> response) {
        return response != null && WeixinpayStatus.TRADE_STATE_SUCCESS.equals(response.get("trade_state"));
    }

    /**
     * 对异步通知结果进行校验
     *
     * @param notify 异步通知参数
     * @param outTradeNo 商家订单号
     * @param totalFee 单位：元
     */
    public static boolean checkAsyncNotify(Map<String, String> notify, String outTradeNo, double totalFee) {
        if (notify == null) {
            return false;
        }

        if (notify.get("sign") == null || !notify.get("sign").equals(SignUtil.sign(notify, WeixinpayCfg.merchant_key))) {
            // 签名错误
            return false;
        }

        // 校验订单信息
        return notify.get("appid").equals(WeixinpayCfg.app_id) && notify.get("mch_id").equals("" + WeixinpayCfg.merchant_id)
                && notify.get("out_trade_no").equals(outTradeNo) && ("" + ((int)((WeixinpayCfg.debug ? 0.01 : totalFee) * 100))).equals(notify.get("total_fee"));
    }

    public static String responseForAnsyNotify(String returnCode, String returnMsg) {
        return "<xml>\n" +
                "<return_code><![CDATA[" + returnCode + "]]></return_code>\n" +
                "<return_msg><![CDATA[" + returnMsg + "]]></return_msg>\n" +
                "</xml>";
    }

}
