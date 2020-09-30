package com.mzx.gulimall.order.feign;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.vo.WareRequestParamTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 订单服务远程查询库存服务.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-28 16:42 周一.
 */
@FeignClient(value = "gulimall-ware")
public interface WareServiceFeign {

    /**
     * 查询给定ids库存.
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/ware/waresku/get/list")
    R getListStock(@RequestBody String[] ids);


}
