package com.mzx.gulimall.order.vo;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-19 13:26 周一.
 */
public class PaySyncVo {

    /**
     * 创建时间.
     */
    private String gmt_create;
    /**
     * 编码格式.
     */
    private String charset;
    /**
     * 不知道.
     */
    private String gmt_payment;
    /**
     * 通知时间.
     */
    private String notify_time;
    /**
     * 订单号名称.
     */
    private String subject;
    /**
     * 签名格式.
     */
    private String sign;
    /**
     * 用户ID.
     * 付者的id.
     */
    private String buyer_id;
    /**
     * 订单的信息,该项可为空.
     */
    private String body;
    /**
     * 本次支付的金额.
     */
    private String invoice_amount;
    /**
     * 本次支付的版本号.
     */
    private String version;
    /**
     * 通知的ID? 这个ID是谁的ID.
     */
    private String notify_id;
    /**
     * 不知道. ?
     */
    private String fund_bill_list;
    /**
     * 通知类型 也不知道干什么的.
     */
    private String notify_type;
    /**
     * 本次支付成功的订单号.
     */
    private String out_trade_no;
    /**
     * 本次支付的实际金额.
     */
    private String total_amount;
    /**
     * 当前订单的交易状态.
     * TRADE_SUCCESS.
     */
    private String trade_status;
    /**
     * 当前订单的流水号.
     */
    private String trade_no;
    /**
     * ？？？？？？
     */
    private String auth_app_id;
    /**
     * 商家实际收到的金额.
     */
    private String receipt_amount;
    /**
     * ???.
     */
    private String point_amount;
    /**
     * 本次支付应用的ID.
     */
    private String app_id;
    /**
     * 本次订单最终实际支付的金额.
     */
    private String buyer_pay_amount;
    /**
     * 本次订单签名的类型.
     */
    private String sign_type;
    /**
     * 商家的ID到底是个什么东西?
     */
    private String seller_id;

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getGmt_payment() {
        return gmt_payment;
    }

    public void setGmt_payment(String gmt_payment) {
        this.gmt_payment = gmt_payment;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(String invoice_amount) {
        this.invoice_amount = invoice_amount;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getFund_bill_list() {
        return fund_bill_list;
    }

    public void setFund_bill_list(String fund_bill_list) {
        this.fund_bill_list = fund_bill_list;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getAuth_app_id() {
        return auth_app_id;
    }

    public void setAuth_app_id(String auth_app_id) {
        this.auth_app_id = auth_app_id;
    }

    public String getReceipt_amount() {
        return receipt_amount;
    }

    public void setReceipt_amount(String receipt_amount) {
        this.receipt_amount = receipt_amount;
    }

    public String getPoint_amount() {
        return point_amount;
    }

    public void setPoint_amount(String point_amount) {
        this.point_amount = point_amount;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getBuyer_pay_amount() {
        return buyer_pay_amount;
    }

    public void setBuyer_pay_amount(String buyer_pay_amount) {
        this.buyer_pay_amount = buyer_pay_amount;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }
}
