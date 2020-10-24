package com.mzx.gulimall.pay.feign;

import com.mzx.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 库存服务的远程接口.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-24 14:40 周六.
 */
@FeignClient(value = "gulimall-ware")
public interface WareServiceFeign {

    /**
     * 真实修改库存容量的接口.
     * <p>
     * 将库存详情单的锁定状态改为已扣除状态.
     *
     * 库存服务接受到的这个值一直是空值.....
     *
     * @param orderSn
     * @return
     */
    @PostMapping(value = "/ware/waresku/post/update/order/ware")
    R updateSkuWare(@RequestParam(value = "orderSn") String orderSn);

}
