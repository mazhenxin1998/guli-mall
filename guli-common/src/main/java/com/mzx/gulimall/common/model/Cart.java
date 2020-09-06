package com.mzx.gulimall.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 整个购物车.
 * <p>
 * 有个疑问: 当我使用lombok的时候,如果我添加了get\set的注解,但是我有的方法是自己需要重写的，那么当我调用get、set的时候使用的是哪个方法呢?
 * 测试结果: 如果自己重写了自己的Get、Set方法那么就使用自己的，如果没有重写那就使用lombok自动生成的方法.
 *
 * @author ZhenXinMa
 * @date 2020/9/6 10:58
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private List<CartItem> items;
    /**
     * 所有商品的数量.
     */
    private Integer count;
    /**
     * 所有商品类型总的之和.
     */
    private Integer countType;

    /**
     * 自定义计算方法.
     */
    private BigDecimal totalPrice;

    /**
     * 减免价格.
     */
    private BigDecimal reduce = new BigDecimal(0);

    /**
     * 计算当前Cart商品总价.
     *
     * @return
     */
    public BigDecimal getTotalPrice() {

        // 应该遍历所有的item,然后将其所有的价格进行相加.
        BigDecimal total = new BigDecimal(0);
        if (this.items != null && items.size() > 0) {

            // 不需要收集.
            items.stream().map(item -> {

                // 计算总价.
                // 这里需要进行判断吗? 应该是不需要的.
                // getTotalPrice要么返回0要么返回其总价.
                total.add(item.getTotalPrice());
                return item;
            });

            // 将计算之后的结果赋值给当前Cart的总价格.
            // 应该将计算之后的总结果减去当前优惠价格.
            this.totalPrice = total.subtract(this.reduce);

        }

        return this.totalPrice;

    }

}
