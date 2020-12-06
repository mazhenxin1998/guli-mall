package com.mzx.gulimall.seckill.vo;

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
public class SeckillSessionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
    /**
     *
     */
    private String name;
    /**
     * 当前秒杀会话开始时间.
     */
    private Date startTime;
    /**
     * 当前秒杀会话结束时间.
     */
    private Date endTime;
    /**
     *
     */
    private Integer status;
    /**
     *
     */
    private Date createTime;

    /**
     * 当前秒杀会话关联的所有商品.
     */
    private List<SeckillSkuRelationEntity> relations;


}
