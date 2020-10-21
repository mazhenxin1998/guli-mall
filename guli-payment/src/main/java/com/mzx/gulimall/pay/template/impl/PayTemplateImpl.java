package com.mzx.gulimall.pay.template.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.mzx.gulimall.pay.config.AliPayConfig;
import com.mzx.gulimall.pay.service.PayService;
import com.mzx.gulimall.pay.template.PayTemplate;
import com.mzx.gulimall.pay.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-19 13:38 周一.
 */
@Component(value = "payTemplateImpl")
public class PayTemplateImpl implements PayTemplate {

    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    @Qualifier(value = "aliPayServiceImpl")
    private PayService payService;

    @Override
    public String pay(PayVo payVo) throws AlipayApiException {

        // 1. 根据支付宝的配置生成一个支付宝客户端.
        AlipayClient client = new DefaultAlipayClient(aliPayConfig.getGatewayUrl(), aliPayConfig.getAppId(),
                aliPayConfig.getMerchant_private_key(), "json", aliPayConfig.getCharset(),
                aliPayConfig.getAlipay_public_key(), aliPayConfig.getSign_type());

        // 2. 创建一个支付请求,设置请求参数 下面配置的两个请求参数没有比对成功.
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // 2.1 设置异步返回url.
        request.setNotifyUrl(aliPayConfig.getNotify_url());
        // 2.2 设置支付成功的回调方法.
        request.setReturnUrl(aliPayConfig.getReturn_url());
        // 3. 配置content.
        String out_trade_no = payVo.getOut_trade_no();
        // 本次支付金额.
        String total_amount = payVo.getTotal_amount();
        // 商品描述可空.
        String body = payVo.getBody();
        // 订单名称.
        String subject = payVo.getSubject();
        request.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        return client.pageExecute(request).getBody();

    }

}
