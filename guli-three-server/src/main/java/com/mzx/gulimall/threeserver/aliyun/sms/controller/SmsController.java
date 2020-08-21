package com.mzx.gulimall.threeserver.aliyun.sms.controller;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.threeserver.aliyun.sms.service.SmsService;
import com.mzx.gulimall.threeserver.vo.SmsParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 15:55
 */
@RestController
@RequestMapping(value = "/thirdparty/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    /**
     * 由于这个是短信发送接口,所以说参数我只需要将接受手机号码和验证码.
     * 如果消息发送成功,那么返回的Code值为0. 如果发送失败那么返回10001和10002两种错误.
     * 10001表示传进来的参数不合法。 10002表示在发送的过程中出现了异常.
     * <p>
     * <p>
     * 使用注解@RequestBody来接受JSON格式的数据.
     * 使用注解@Valid 来开始JSR303的校验.
     * 在其校验的参数后面紧跟着BindingResult来获取校验出错的异常.
     *
     * @return
     */
    @PostMapping(value = "/send")
    public R sendSms(@RequestBody @Valid SmsParamVo param, BindingResult bindingResult) {

        System.out.println("sendSms方法发生了");
        return smsService.sendSmsDx(param);
    }


}
