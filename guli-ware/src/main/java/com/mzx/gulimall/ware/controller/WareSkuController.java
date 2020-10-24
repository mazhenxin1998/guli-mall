package com.mzx.gulimall.ware.controller;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.ware.entity.WareSkuEntity;
import com.mzx.gulimall.ware.service.WareSkuService;
import com.mzx.gulimall.ware.vo.LockStockResult;
import com.mzx.gulimall.ware.vo.WareSkuLockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 商品库存
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-25 14:15:39
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {

    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = wareSkuService.queryPageDetails(params);
        return R.ok().put("page", page);

    }

    /**
     * 参数校验注解还是有问题的.
     *
     * @param wareSkuLockVo
     * @return
     */
    @PostMapping(value = "/post/lock/stock")
    public R lockStock(@RequestBody WareSkuLockVo wareSkuLockVo) {

        List<LockStockResult> stockResults = wareSkuService.lockStock(wareSkuLockVo);
        String jsonString = JSON.toJSONString(stockResults);
        // 远程查询返回的结果是json里面保存的List集合.
        // 如果锁定失败, 那么这里将不会进行返回. 不会进行返回那么在订单服务中, 判断返回值将会是空, 如果是空可以直接响应用户下单失败.
        // 并且在订单服务中，应该从R中获取到code状态码来进行判断.
        return R.ok().put("data", jsonString);

    }

    /**
     * 报错找不到List初始化函数.
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/get/list")
    public R getListStock(@RequestBody String[] ids) {

        // 批量查询ids对应的库存数量.
        // data属性可能为空值.
        // 现在需要将ids转换为List.
        System.out.println("ids的值是: " + ids);
        List<String> list = Arrays.asList(ids);
        List<Long> listIds = list.stream().map(item -> {

            return Long.valueOf(item);

        }).collect(Collectors.toList());
        return wareSkuService.getListStock(listIds);

    }

    @GetMapping(value = "/get/{skuId}")
    public boolean getStock(@PathVariable Long skuId) {

        return wareSkuService.getStock(skuId);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);

        return R.ok();
    }

    @PostMapping(value = "/post/update/order/ware")
    public R updateSkuWare(@RequestParam(value = "orderSn") String orderSn) {

        // orderSn一直是空值.
        return wareSkuService.updateSkuWare(orderSn);

    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
