package com.mzx.gulimall.im.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ZhenXinMa
 * @date 2020/8/18 22:52
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageVo {

    private String toName;
    private String fromName;
    private String message;

}
