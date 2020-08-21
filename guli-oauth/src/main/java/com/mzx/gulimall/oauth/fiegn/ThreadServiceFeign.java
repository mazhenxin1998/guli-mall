package com.mzx.gulimall.oauth.fiegn;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.vo.SmsParamVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 18:43
 */
@FeignClient(value = "gulimall-three-server")
public interface ThreadServiceFeign {

    /**
     * 调用第三方服务.
     * @param param
     * @return
     */
    @PostMapping(value = "/thirdparty/sms/send")
    R sendSms(@RequestBody SmsParamVo param);


}
