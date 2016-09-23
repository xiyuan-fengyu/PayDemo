package com.xiyuan.pay.weixin;

import com.xiyuan.pay.weixin.model.PayReq;
import com.xiyuan.pay.weixin.request.WeixinpayCreateRequest;
import com.xiyuan.pay.weixin.request.WeixinpayQueryRequest;
import com.xiyuan.pay.weixin.response.WeixinpayAsyncNotifyResult;
import com.xiyuan.pay.weixin.response.WeixinpayCreateResult;
import com.xiyuan.pay.weixin.response.WeixinpayQueryResult;
import com.xiyuan.pay.weixin.util.SignUtil;
import com.xiyuan.pay.weixin.value.WeixinpayStatus;

/**
 * Created by xiyuan_fengyu on 2016/8/26.
 */
public class Weixinpay {

    public static PayReq createPayReq(int id, String body, String detail, String out_trade_no, double total_fee, String spbill_create_ip) {
        WeixinpayCreateRequest request = new WeixinpayCreateRequest(id, body, detail, out_trade_no, total_fee, spbill_create_ip);
        WeixinpayCreateResult result = request.execute(WeixinpayCreateResult.class);
        if (SignUtil.sign(result, WeixinpayCfg.mch_key).equals(result.getSign()) && result.getReturn_code().equals(WeixinpayStatus.success) && result.getResult_code().equals(WeixinpayStatus.success)) {
            //预创建支付订单成功
            return new PayReq(result.getPrepay_id());
        }
        else return null;
    }

    public static PayReq createPayReqWithPrepayId(String prepayId) {
        return new PayReq(prepayId);
    }

    /**
     * 对异步通知结果进行校验
     * @param notify
     * @param out_trade_no
     * @param total_fee   单位分
     * @return
     */
    public static boolean notifyCheck(WeixinpayAsyncNotifyResult notify, String out_trade_no, int total_fee) {
        if (notify == null) {
            return false;
        }

        if (!SignUtil.sign(notify, WeixinpayCfg.mch_key).equals(notify.getSign())) {
            //签名错误
            return false;
        }

        //校验订单信息
        return notify.getAppid().equals(WeixinpayCfg.app_id) && notify.getMch_id().equals("" + WeixinpayCfg.mch_id) && notify.getOut_trade_no().equals(out_trade_no) && notify.getTotal_fee() == total_fee;
    }

    public static WeixinpayQueryResult queryState(String out_trade_no) {
        WeixinpayQueryRequest request = new WeixinpayQueryRequest(out_trade_no);
        WeixinpayQueryResult result = request.execute(WeixinpayQueryResult.class);
        if (SignUtil.sign(result, WeixinpayCfg.mch_key).equals(result.getSign())) {
            return result;
        }
        else return null;
    }

    public static String generateResultForNotify(String return_code, String return_msg) {
        StringBuilder strBld = new StringBuilder();
        strBld.append("<xml>\n");
        strBld.append("    <return_code><![CDATA[").append(return_code).append("]]></return_code>\n");
        strBld.append("    <return_msg><![CDATA[").append(return_msg).append("]]></return_msg>\n");
        strBld.append("</xml>");
        return strBld.toString();
    }

}
