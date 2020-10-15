package com.mzx.gulimall.common.order;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-15 22:23 周四.
 */
@Data
@ToString
public class Result implements Serializable {

    private OrderTo order;
    private Integer code;
    private String msg;

}
