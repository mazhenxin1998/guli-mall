package com.mzx.gulimall.threeserver.aliyun.sms.service;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.threeserver.vo.SmsParamVo;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 15:56
 */
public interface SmsService {
    /**
     * 发送指定的验证码给指定的用户.
     * @param param
     * @return
     */
    R send(SmsParamVo param);

    /**
     * 使用DX发送短信验证码.
     * @param param
     * @return
     */
    R sendSmsDx(SmsParamVo param);
}
