package com.mzx.gulimall.threeserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 17:01
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SmsDataVo {

    private String Message;
    private String RequestId;
    private String BizId;
    private String Code;

}
