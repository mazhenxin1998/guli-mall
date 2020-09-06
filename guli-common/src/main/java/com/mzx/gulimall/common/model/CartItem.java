package com.mzx.gulimall.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 一个该对象表示一个购物项.
 *
 * @author ZhenXinMa
 * @date 2020/9/6 10:50
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Long skuId;
    /**
     * 默认为true.
     */
    private boolean check = true;
    private String title;
    private String image;
    /**
     * 可要也可不要.
     */
    private List<String> skuAttr;
    /**
     * 引用类型的零值是null.
     */
    private BigDecimal price;
    private Integer count;
    /**
     * 计算当前商品的总价,这个对应一件商品我可以一次购买多件,也就是对应的购买数量.
     * Cart计算总价的时候应该是计算这个.
     */
    private BigDecimal totalPrice;


    public BigDecimal getTotalPrice() {

        // 计算总的价格之前应该确保count和price不为空.
        if (this.price != null) {

            // 可以不需要考虑count,因为其零值是0;
            BigDecimal bigDecimal = new BigDecimal(count);
            BigDecimal totalPrice = price.multiply(bigDecimal);
            return totalPrice;

        }

        // 如果当前价格为空,那么就返回一个0值吧.
        return new BigDecimal(0);

    }


}
