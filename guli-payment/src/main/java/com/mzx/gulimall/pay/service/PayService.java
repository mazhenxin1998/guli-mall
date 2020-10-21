package com.mzx.gulimall.pay.service;

import com.mzx.gulimall.pay.vo.PayVo;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-19 15:00 周一.
 */
public interface PayService {

    /**
     * 支付接口.
     *
     * @param vo 本次要进行支付的订单号.
     * @return
     */
    String pay(PayVo vo);

    /**
     * 通过订单号构建出该订单的支付信息.
     *
     * @param orderSn
     * @return
     */
    PayVo buildPayVo(String orderSn);
}
