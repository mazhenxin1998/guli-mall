package com.mzx.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:28:33
 */
@Data
@TableName("ums_member_statistics_info")
public class MemberStatisticsInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long memberId;
	/**
	 * 
	 */
	private BigDecimal consumeAmount;
	/**
	 * 
	 */
	private BigDecimal couponAmount;
	/**
	 * 
	 */
	private Integer orderCount;
	/**
	 * 
	 */
	private Integer couponCount;
	/**
	 * 
	 */
	private Integer commentCount;
	/**
	 * 
	 */
	private Integer returnOrderCount;
	/**
	 * 
	 */
	private Integer loginCount;
	/**
	 * 
	 */
	private Integer attendCount;
	/**
	 * 
	 */
	private Integer fansCount;
	/**
	 * 
	 */
	private Integer collectProductCount;
	/**
	 * 
	 */
	private Integer collectSubjectCount;
	/**
	 * 
	 */
	private Integer collectCommentCount;
	/**
	 * 
	 */
	private Integer inviteFriendCount;

}
