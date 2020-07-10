package com.mzx.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
@Data
@TableName("sms_home_adv")
public class SmsHomeAdvEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private String name;
	/**
	 * ͼƬ
	 */
	private String pic;
	/**
	 * 
	 */
	private Date startTime;
	/**
	 * 
	 */
	private Date endTime;
	/**
	 * ״̬
	 */
	private Integer status;
	/**
	 * 
	 */
	private Integer clickCount;
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String note;
	/**
	 * 
	 */
	private Integer sort;
	/**
	 * 
	 */
	private Long publisherId;
	/**
	 * 
	 */
	private Long authId;

}
