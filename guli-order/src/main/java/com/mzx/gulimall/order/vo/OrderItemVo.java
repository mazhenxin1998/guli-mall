package com.mzx.gulimall.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 该VO其实就是CartItem。
 *
 * @author ZhenXinMa.
 * @slogan 浮生若梦, 若梦非梦, 浮生何梦? 如梦之梦.
 * @create 2020-09-16 21:53 周三.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemVo implements Serializable {

    private Long skuId;
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


}
