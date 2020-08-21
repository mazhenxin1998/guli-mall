package com.mzx.gulimall.threeserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 15:58
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SmsParamVo {

    /**
     * 不允许出现空值 null和""都不允许.
     */
    @NotNull(message = "手机号码不能为空")
    private Long phone;
    /**
     * 不允许出现空值.
     */
    private String code;


}
