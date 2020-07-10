package com.mzx.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * 
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@Data
@TableName("pms_attr")
public class AttrEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long attrId;
	/**
	 * 
	 */
	private String attrName;
	/**
	 * 
	 */
	private Integer searchType;
	/**
	 * 
	 */
	private String icon;
	/**
	 * 
	 */
	private String valueSelect;
	/**
	 * 
	 */
	private Integer attrType;
	/**
	 * 
	 */
	private Long enable;
	/**
	 * 
	 */
	private Long catelogId;
	/**
	 * 
	 */
	private Integer showDesc;

}
