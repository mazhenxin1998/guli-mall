package com.mzx.gulimall.order.vo;

import com.mzx.gulimall.order.entity.OrderEntity;
import com.mzx.gulimall.order.entity.OrderItemEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-04 20:01 周日.
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateTo {

    /**
     * 订单.
     */
    OrderEntity order;
    /**
     * 订单的实体类.
     */
    List<OrderItemEntity> orderItems;
    /**
     * 本次应该交付的金额.
     */
    BigDecimal payPrice;
    /**
     * 计算运费.
     * 本项目中不进行计算, 默认值为0.
     */
    BigDecimal fare = new BigDecimal(0);

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }
}
