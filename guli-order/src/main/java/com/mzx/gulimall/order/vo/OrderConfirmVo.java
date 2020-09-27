package com.mzx.gulimall.order.vo;

import lombok.AllArgsConstructor;
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
     * 直接查询购物车中被选中的数据.
     */
    List<OrderItemVo> items;

    /**
     * 优惠券信息.
     * 暂时只支持豆豆抵消优惠,或者没有优惠活动.
     * 优惠功能暂时不做了.
     */
    private Integer integration;

    /**
     * 订单幂等验证属性.
     */
    private String token;

    /**
     * 没有抵消优惠券的前提下.
     */
    private BigDecimal total = new BigDecimal(0);
    /**
     * 抵消了优惠券情况下的实际支付的价格.
     */
    private BigDecimal payPrice = new BigDecimal(0);

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 重新计算应该支付的钱.
     * 其应该总共的金额减去优惠券.
     */
    public void setPayPrice() {

        this.payPrice = this.total.subtract(new BigDecimal(this.integration));

    }

    /**
     * 重新计算总共支付的金额.
     * 用户本次订单上商品的交易总额.
     */
    public void setTotal() {

        items.stream().forEach(item -> {

            BigDecimal totalPrice = item.getTotalPrice();
            this.total = this.total.add(totalPrice);

        });
        this.payPrice = this.total.subtract(new BigDecimal(this.integration));
        System.out.println("订单优惠之后的价格是: " + this.payPrice);
        System.out.println("订单在没有进行优惠时的价格: : " + this.payPrice);

    }


    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public List<MemberAddressVo> getAddress() {
        return address;
    }

    public void setAddress(List<MemberAddressVo> address) {
        this.address = address;
    }

    public List<OrderItemVo> getItems() {
        return items;
    }

    public void setItems(List<OrderItemVo> items) {
        this.items = items;
    }

    public Integer getIntegration() {
        return integration;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }
}
