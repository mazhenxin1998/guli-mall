package com.mzx.gulimall.pay.template;

import com.alipay.api.AlipayApiException;
import com.mzx.gulimall.pay.vo.PayVo;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-19 13:39 周一.
 */
public interface PayTemplate {

    /**
     * 当前系统支付接口.
     * 在30分钟之内如果用户还么有下单,那么以后再去支付宝进行支付那么将会支付失败.
     * <p>
     * 可以有多中该实现类.
     *
     * @param vo 要进行支付的详细信息.
     * @return
     * @throws AlipayApiException
     */
    String pay(PayVo vo) throws AlipayApiException;

}
