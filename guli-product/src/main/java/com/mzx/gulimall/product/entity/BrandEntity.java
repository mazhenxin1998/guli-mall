package com.mzx.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.mzx.gulimall.product.valid.NotList;
import com.mzx.gulimall.product.valid.Select;
import com.sun.xml.internal.ws.api.model.MEP;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Ʒ
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
@Data
@ApiModel
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Ʒ
     */
    @TableId
    @ApiModelProperty(value = "品牌ID，数据库主键", dataType = "Long")
    private Long brandId;
    /**
     * 使用了Groups表示只有在开启该组校验的时候才会对该字段进行NotEmpty校验.
     *
     * @NotNull 注解表示不是NULL的元素.
     * @NotEmpty 表示：不能为NULL字符串也不能为""
     */
    @ApiModelProperty(value = "品牌名字", dataType = "String")
    @NotEmpty(message = "品牌名字不能为空", groups = {Select.class})
    private String name;
    /**
     * Ʒ
     */
    @ApiModelProperty(value = "品牌Logo地址:URL", dataType = "URL")
    @URL(message = "logo必须是一个合法的URL地址", groups = {Select.class})
    private String logo;
    /**
     *
     */
    @ApiModelProperty(value = "品牌的描述", dataType = "String")
    private String descript;
    /**
     * 只要传进来的值在给定的指定范围内则校验会成功!
     */
    @ApiModelProperty(value = "品牌是否显示的状态标志位", dataType = "Integer")
    @NotList(values = {0, 1})
    private Integer showStatus;
    /**
     *
     */
    @ApiModelProperty(value = "品牌首字母", dataType = "String")
    private String firstLetter;
    /**
     *
     */
    @ApiModelProperty(value = "品牌排序字段", dataType = "Integer")
    private Integer sort;

}
