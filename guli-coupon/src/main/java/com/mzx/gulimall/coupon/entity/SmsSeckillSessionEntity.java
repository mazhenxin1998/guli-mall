package com.mzx.gulimall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
@Data
@TableName("sms_seckill_session")
public class SmsSeckillSessionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 场次名称.
     */
    private String name;
    /**
     * 每日开始时间.
     */
    private Date startTime;
    /**
     * 每日结束时间.
     */
    private Date endTime;
    /**
     * 当前秒杀状态.
     */
    private Integer status;
    /**
     * 会话创建时间.
     */
    private Date createTime;

    /**
     * 当前会话关联的商品id.
     *
     * @TableField(exist = true)表示当前字段是应用程序自己扩展的字段,不是表中字段.
     */
    @TableField(exist = true)
    private List<SmsSeckillSkuRelationEntity> relations;

}
