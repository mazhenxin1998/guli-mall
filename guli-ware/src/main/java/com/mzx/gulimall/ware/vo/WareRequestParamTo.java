package com.mzx.gulimall.ware.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-28 21:09 周一.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WareRequestParamTo {

    @NotEmpty
    private List<Long> ids;

}
