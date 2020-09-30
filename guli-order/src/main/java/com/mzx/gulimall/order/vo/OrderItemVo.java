package com.mzx.gulimall.order.vo;

import lombok.AllArgsConstructor;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemVo implements Serializable {

    Long skuId;
    String title;
    String image;

    /**
     * 可要也可不要.
     */
    List<String> skuAttr;

    String attr;

    /**
     * 引用类型的零值是null.
     */
    BigDecimal price;
    Integer count;
    Integer repertory;
    /**
     * 计算当前商品的总价,这个对应一件商品我可以一次购买多件,也就是对应的购买数量.
     * Cart计算总价的时候应该是计算这个.
     */
    BigDecimal totalPrice;

    public void setSkuAttr(List<String> list) {

        this.skuAttr = list;

    }

    /**
     * 将其转换成.
     */
    public void convert() {

        StringBuilder builder = new StringBuilder();
        this.skuAttr.forEach(item -> {

            builder.append(item);
            builder.append(" ");

        });

        this.attr = builder.toString();

    }

    public List<String> getSkuAttr() {
        return skuAttr;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
