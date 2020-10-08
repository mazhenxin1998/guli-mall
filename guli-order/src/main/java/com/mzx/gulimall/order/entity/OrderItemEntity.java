package com.mzx.gulimall.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单项信息
 * 
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 16:44:36
 */
@Data
@TableName("oms_order_item")
public class OrderItemEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id数据库自增.
	 */
	@TableId
	Long id;
	/**
	 * order_id
	 */
	Long orderId;
	/**
	 * order_sn
	 */
	String orderSn;
	/**
	 * spu_id
	 */
	Long spuId;
	/**
	 * spu_name
	 */
	String spuName;
	/**
	 * spu_pic
	 */
	String spuPic;
	/**
	 * 品牌
	 */
	String spuBrand;
	/**
	 * 商品分类id
	 */
	Long categoryId;
	/**
	 * 商品sku编号
	 */
	Long skuId;
	/**
	 * 商品sku名字
	 */
	String skuName;
	/**
	 * 商品sku图片
	 */
	String skuPic;
	/**
	 * 商品sku价格
	 */
	BigDecimal skuPrice;
	/**
	 * 商品购买的数量
	 */
	Integer skuQuantity;
	/**
	 * 商品销售属性组合（JSON）
	 */
	String skuAttrsVals;
	/**
	 * 商品促销分解金额
	 */
	BigDecimal promotionAmount;
	/**
	 * 优惠券优惠分解金额
	 */
	BigDecimal couponAmount;
	/**
	 * 积分优惠分解金额
	 */
	BigDecimal integrationAmount;
	/**
	 * 该商品经过优惠后的分解金额
	 */
	BigDecimal realAmount;
	/**
	 * 赠送积分
	 */
	Integer giftIntegration;
	/**
	 * 赠送成长值
	 */
	Integer giftGrowth;

}
