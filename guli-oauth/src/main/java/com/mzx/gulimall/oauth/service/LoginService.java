package com.mzx.gulimall.oauth.service;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.vo.SmsParamVo;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 21:18
 */
public interface LoginService {

    /**
     * 调用远程服务发送手机短信.
     *
     * @param param
     * @return
     */
    R sendSms(SmsParamVo param);
}
