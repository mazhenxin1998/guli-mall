package com.mzx.gulimall.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-16 21:52 周三.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmVo implements Serializable {

    /**
     * 封住了用户的所有可用地址列表.
     */
    List<MemberAddressVo> address;
    /**
     * 封住了本次要提交订单的的货物.
     */
    List<OrderItemVo> items;

    /**
     * 优惠券信息.
     * 暂时只支持豆豆抵消优惠,或者没有优惠活动.
     */
    private Integer integration;
    /**
     * 没有抵消优惠券的前提下.
     */
    private BigDecimal total;
    /**
     * 抵消了优惠券情况下的实际支付的价格.
     */
    private BigDecimal payPrice;

}
