package com.mzx.gulimall.coupon.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-26 21:30 周一.
 */
@Data
@ToString
public class SecKillSessionVo implements Serializable {

    private Long id;
    /**
     *
     */
    private String name;
    /**
     * ÿ
     */
    private String startTime;
    /**
     * ÿ
     */
    private String endTime;
    /**
     *
     */
    private Integer status;
    /**
     *
     */
    private String createTime;


}
