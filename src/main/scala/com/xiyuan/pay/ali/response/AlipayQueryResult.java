package com.xiyuan.pay.ali.response;

/**
 * Created by xiyuan_fengyu on 2016/8/29.
 */
public class AlipayQueryResult {

    private Info alipay_trade_query_response;

    private String sign;

    public Info getAlipay_trade_query_response() {
        return alipay_trade_query_response;
    }

    public void setAlipay_trade_query_response(Info alipay_trade_query_response) {
        this.alipay_trade_query_response = alipay_trade_query_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public class Info {
        private String code;
        private String msg;
        private String buyer_logon_id;
        private Double buyer_pay_amount;
        private String buyer_user_id;
        private Double invoice_amount;
        private String open_id;
        private String out_trade_no;
        private Double point_amount;
        private Double receipt_amount;
        private String send_pay_date;
        private Double total_amount;
        private String trade_no;
        private String trade_status;

        public String getBuyer_logon_id() {
            return buyer_logon_id;
        }

        public void setBuyer_logon_id(String buyer_logon_id) {
            this.buyer_logon_id = buyer_logon_id;
        }

        public Double getBuyer_pay_amount() {
            return buyer_pay_amount;
        }

        public void setBuyer_pay_amount(Double buyer_pay_amount) {
            this.buyer_pay_amount = buyer_pay_amount;
        }

        public String getBuyer_user_id() {
            return buyer_user_id;
        }

        public void setBuyer_user_id(String buyer_user_id) {
            this.buyer_user_id = buyer_user_id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Double getInvoice_amount() {
            return invoice_amount;
        }

        public void setInvoice_amount(Double invoice_amount) {
            this.invoice_amount = invoice_amount;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getOpen_id() {
            return open_id;
        }

        public void setOpen_id(String open_id) {
            this.open_id = open_id;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public Double getPoint_amount() {
            return point_amount;
        }

        public void setPoint_amount(Double point_amount) {
            this.point_amount = point_amount;
        }

        public Double getReceipt_amount() {
            return receipt_amount;
        }

        public void setReceipt_amount(Double receipt_amount) {
            this.receipt_amount = receipt_amount;
        }

        public String getSend_pay_date() {
            return send_pay_date;
        }

        public void setSend_pay_date(String send_pay_date) {
            this.send_pay_date = send_pay_date;
        }

        public Double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(Double total_amount) {
            this.total_amount = total_amount;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public String getTrade_status() {
            return trade_status;
        }

        public void setTrade_status(String trade_status) {
            this.trade_status = trade_status;
        }
    }

}
