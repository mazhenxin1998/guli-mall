package com.mzx.gulimall.order.feign;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    /**
     * 远程锁定库存.
     *
     * @param wareSkuLockVo
     * @return
     */
    @PostMapping(value = "/ware/waresku/post/lock/stock")
    R lockStock(@RequestBody WareSkuLockVo wareSkuLockVo);

    /**
     * 远程请求库存服务回滚日志.
     *
     * @param messageId
     * @return
     */
    @PostMapping(value = "/post/{messageId}")
    R rollBack(@PathVariable(value = "messageId") String messageId);


}
