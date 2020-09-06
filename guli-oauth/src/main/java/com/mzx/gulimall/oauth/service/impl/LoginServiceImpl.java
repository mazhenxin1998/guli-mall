package com.mzx.gulimall.oauth.service.impl;

import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.entity.MemberEntity;
import com.mzx.gulimall.oauth.fiegn.MemberServiceFeign;
import com.mzx.gulimall.oauth.fiegn.ThreadServiceFeign;
import com.mzx.gulimall.oauth.service.LoginService;
import com.mzx.gulimall.oauth.vo.LoginVo;
import com.mzx.gulimall.oauth.vo.MemberVo;
import com.mzx.gulimall.oauth.vo.SmsParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 21:18
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private ThreadServiceFeign threadServiceFeign;

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @Override
    public R sendSms(SmsParamVo param) {

        R r = threadServiceFeign.sendSms(param);
        System.out.println(r.toString());
        return r;
    }

    @Override
    public MemberEntity getUserById(Long userId) {

        return memberServiceFeign.getMemberInfo(userId);
    }

    @Override
    public MemberResultVo login(LoginVo vo) {

        // 登录校验》
        // 手机号必须是唯一: 那么就查询手机号. 不应该仅仅返回一个密码， 应该返回的是整个Member实体类.
        // 应该将该MemberResultVo放到session会话中.
        MemberResultVo resultVo = memberServiceFeign.getPasswordByPhone(vo.getPhone());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(vo.getPassword(), resultVo.getPassword())) {

            // 校验通过. 校验通过之后
            // 还需要查询该
            return resultVo;
        } else{

            return null;
        }

    }
}
