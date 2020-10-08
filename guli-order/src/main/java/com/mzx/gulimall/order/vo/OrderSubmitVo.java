package com.mzx.gulimall.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
     * 这个值为什么是空值呢?
     */
    @NotNull
    Long addrId;
    /**
     * 支付类型. 在线支付. 也只有这一种类型.
     * 0-表示线下支付.
     * 1-表示线上支付.
     * 默认是线上支付.
     */
    Integer payType = 1;
    /**
     * 订单防重token.
     * 在订单页面生成的时候,将会生成一个.
     */
    @NotEmpty
    String orderToken;
    /**
     * 订单提交的时候提交的总的支付金额.
     * 实际支付的时候需要重新计算.
     * 重新从购物车计算.
     */
    @NotNull
    BigDecimal payPrice;

    /**
     * 当前下订单的备注.
     */
    String note;

}
