package com.mzx.gulimall.pay.vo;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 支付宝支付需要的vo.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-19 13:24 周一.
 */
public class PayVo implements Serializable {

    /**
     * 对外显示的订单号 必填.
     */
    @NotEmpty
    private String out_trade_no;

    /**
     * 订单名称,必填 可以是第一个商品的名称.
     */
    @NotEmpty
    private String subject;

    /**
     * 本次订单实际支付的价格 必填.
     */
    @NotEmpty
    private String total_amount;

    /**
     * 商品描述,可为空. 非必填.
     */
    private String body;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
