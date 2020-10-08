package com.mzx.gulimall.order.feign;

import com.mzx.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 17:03 周六.
 */
@FeignClient(value = "gulimall-cart")
public interface CartServiceFeign {

    /**
     * 远程查询购物车服务,获取当前用户选中的商品.
     *
     * @return
     */
    @GetMapping(value = "/cart/get/checked/items")
    R getCheckedCartItems();

}
