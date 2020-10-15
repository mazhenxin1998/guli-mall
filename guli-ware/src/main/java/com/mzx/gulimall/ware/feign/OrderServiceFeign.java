package com.mzx.gulimall.ware.feign;

import com.mzx.gulimall.common.order.Result;
import com.mzx.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-14 17:44 周三.
 */
@FeignClient(value = "gulimall-order")
public interface OrderServiceFeign {

    /**
     * 远程请求order服务通过订单号查询订单.
     *
     * @param orderSn
     * @return
     */
    @GetMapping(value = "/order/order/get/{orderSn}")
    Result getOrderByOrderSn(@PathVariable(value = "orderSn") String orderSn);

}
