package com.mzx.gulimall.pay.service;

import com.alipay.api.AlipayApiException;
import com.mzx.gulimall.pay.vo.PaySyncVo;
import com.mzx.gulimall.pay.vo.PayVo;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 应该先进行验签.
     * 而后在进行订单操作.
     * @param vo
     * @param request
     * @return
     * @throws  AlipayApiException
     */
    String doPaySyncResult(PaySyncVo vo, HttpServletRequest request) throws AlipayApiException;
}
