package com.mzx.gulimall.product.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhenXinMa
 * @date 2020/7/17 16:52
 */
@Data
public class AttrResponseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
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
    /**
     * 属性分组ID.
     */
    private Long attrGroupId;
    /**
     * 可选值模式.
     */
    private Long valueType;
    /**
     * 返回分组名字.
     */
    private String groupName;

    /**
     * 返回分类名字.
     */
    private String catelogName;

}
