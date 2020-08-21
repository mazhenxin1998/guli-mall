package com.mzx.gulimall.oauth.service;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.vo.MemberVo;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 22:29
 */
public interface RegService {

    /**
     * 调用会员服务进行用户注册.
     *
     * @param member
     * @return
     */
    R reg(MemberVo member);
}
