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

    private Long userID;
    private String fromName;
    private String message;
    /**
     * 表示当前是用户还是客服的标志位.
     * true: 表示是客服.
     * false: 表示是用户.
     */
    private boolean flag;

}
