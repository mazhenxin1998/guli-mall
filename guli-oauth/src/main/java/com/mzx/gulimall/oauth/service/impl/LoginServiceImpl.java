package com.mzx.gulimall.oauth.service.impl;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.fiegn.ThreadServiceFeign;
import com.mzx.gulimall.oauth.service.LoginService;
import com.mzx.gulimall.oauth.vo.SmsParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 21:18
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private ThreadServiceFeign threadServiceFeign;

    @Override
    public R sendSms(SmsParamVo param) {

        R r = threadServiceFeign.sendSms(param);
        System.out.println(r.toString());
        return r;
    }
}
