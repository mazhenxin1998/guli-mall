package com.mzx.gulimall.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.mzx.gulimall.common.order.OrderTo;
import com.mzx.gulimall.common.order.Result;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.pay.config.AliPayConfig;
import com.mzx.gulimall.pay.feign.OrderServiceFeign;
import com.mzx.gulimall.pay.feign.WareServiceFeign;
import com.mzx.gulimall.pay.service.PayService;
import com.mzx.gulimall.pay.template.PayTemplate;
import com.mzx.gulimall.pay.vo.PaySyncVo;
import com.mzx.gulimall.pay.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-19 15:00 周一.
 */
@Service(value = "aliPayServiceImpl")
public class AliPayServiceImpl implements PayService {

    @Autowired
    @Qualifier(value = "payTemplateImpl")
    private PayTemplate payTemplate;

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @Autowired
    private WareServiceFeign wareServiceFeign;

    @Autowired
    private AliPayConfig aliPayConfig;

    @Override
    public String pay(PayVo vo) {

        try {

            // 支付订单之前需要检查当前订单是否过期.
            // 直接进行支付.
            return payTemplate.pay(vo);

        } catch (AlipayApiException e) {

            e.printStackTrace();
            return "调用AliPay出现异常.";

        }

    }

    @Override
    public PayVo buildPayVo(String orderSn) {

        PayVo vo = new PayVo();
        Result feignResult = orderServiceFeign.getOrderByOrderSn(orderSn);
        OrderTo order = feignResult.getOrder();
        vo.setOut_trade_no(orderSn);
        vo.setSubject("小苹果校园超市助手支付页面:");
        // 支付金额保留两位小数,向上取整.
        vo.setTotal_amount(order.getPayAmount().setScale(2, RoundingMode.UP).toString());
        vo.setBody("该用户很懒,下单是没有进行备注.!");
        return vo;

    }

    @Override
    public String doPaySyncResult(PaySyncVo vo, HttpServletRequest request) throws AlipayApiException {

        System.out.println(vo.toString());
        System.out.println( "本次成功交易之后的订单号: " + vo.getOut_trade_no());
        boolean verifySign = this.verifySign(request);
        if (verifySign) {

            // 验证成功.
            System.out.println("验证签名成功.");
            // 第一步 首先需要修改订单状态为已付款状态.
            // 订单在自动释放订单的时候，会手动向支付宝发送一个收单请求.
            // 1表示已付款. 也表示代发货.
            // TODO: 这里又的使用分布式事务. 自己实现一下.
            // TODO: 2020/10/24 这里是有问题的. 
            // 必须保证订单状态的改变和库存的扣减要么都成功,要么都失败.
            // 谷粒商城好像没有做这个.
            R result = orderServiceFeign.updateOrderStatus(vo.getOut_trade_no(), "1");
            if (result.getCode() == 0) {

                // TODO: 2020/10/24 上面调用order服务正常,但是下面调用ware服务出现异常.
                // 这个时候需要回滚订单的状态.
                // 修改库存.
                System.out.println("Pay Server 开始扣减库存.");
                // 为什么传进
                wareServiceFeign.updateSkuWare(vo.getOut_trade_no());
                //修改成功.
                return "success";

            }

            return "error";

        } else {

            // 验签失败.
            System.out.println("验证签名失败.");

            // 只要是不成功的字段随便返回什么都行.
            return "error";

        }

    }

    public boolean verifySign(HttpServletRequest request) throws AlipayApiException {

        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {

            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {

                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);

        }

        //调用SDK验证签名.
        // 异常向上抛出.
        return AlipaySignature.rsaCheckV1(params, aliPayConfig.getAlipay_public_key(),
                aliPayConfig.getCharset(), aliPayConfig.getSign_type());

    }

}
