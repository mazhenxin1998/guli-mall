package com.mzx.gulimall.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-30 22:14 周三.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitVo {

    /**
     * 收货人地址.
     */
    private Long addrId;
    /**
     * 支付类型. 在线支付. 也只有这一种类型.
     */
    private Integer payType;
    /**
     * 订单防重token.
     * 在订单页面生成的时候,将会生成一个.
     */
    private String orderToken;
    /**
     * 订单提交的时候提交的总的支付金额.
     * 实际支付的时候需要重新计算.
     * 重新从购物车计算.
     */
    private BigDecimal payPrice;

    /**
     * 当前下订单的备注.
     */
    private String note;

}
