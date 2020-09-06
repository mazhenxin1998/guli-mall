package com.mzx.gulimall.oauth.service;

import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.entity.MemberEntity;
import com.mzx.gulimall.oauth.vo.LoginVo;
import com.mzx.gulimall.oauth.vo.MemberVo;
import com.mzx.gulimall.oauth.vo.SmsParamVo;

import javax.servlet.http.HttpSession;

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

    /**
     * 通过用户ID查询出该用户所对应的用户信息.
     * @param userId
     * @return
     */
    MemberEntity getUserById(Long userId);

    /**
     * 登录校验.
     * @param vo
     * @return
     */
    MemberResultVo login(LoginVo vo);
}
