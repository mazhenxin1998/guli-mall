package com.mzx.gulimall.pay.feign;

import com.mzx.gulimall.common.order.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-19 15:17 周一.
 */
@FeignClient(value = "gulimall-order")
public interface OrderServiceFeign {

    /**
     * 远程请求order服务获取有关订单orderSn相关信息.
     *
     * @param orderSn
     * @return
     */
    @GetMapping(value = "/order/order/get/{orderSn}")
    Result getOrderByOrderSn(@PathVariable(value = "orderSn") String orderSn);

}
