package com.mzx.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@Data
@TableName("pms_attr_group")
public class AttrGroupEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long attrGroupId;
    /**
     *
     */
    private String attrGroupName;
    /**
     *
     */
    private Integer sort;
    /**
     *
     */
    private String descript;
    /**
     *
     */
    private String icon;
    /**
     *
     */
    private Long catelogId;
    /**
     * 逻辑删除
     */
    private Integer showStatus;
}
