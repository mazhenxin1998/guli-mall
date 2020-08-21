package com.mzx.gulimall.oauth.service.impl;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.service.RegService;
import com.mzx.gulimall.oauth.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 22:30
 */
@Service
public class RegServiceImpl implements RegService {

    
    @Override
    public R reg(MemberVo member) {
        
        /*
         * --------------------------------------------------------
         * 如果底层调用第三方服务, 那么在第三方服务中做具体的调用,
         * 所以说这里不需要做具体的细节.
         * --------------------------------------------------------
         * */

        // 先校验下验证码是否正确.


        return null;
    }
}
