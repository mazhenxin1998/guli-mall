package com.mzx.gulimall.order.controller;

import com.mzx.gulimall.common.order.OrderTo;
import com.mzx.gulimall.common.order.Result;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.entity.OrderEntity;
import com.mzx.gulimall.order.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 订单
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 16:44:36
 */
@RestController
@RequestMapping("order/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = orderService.queryPage(params);
        return R.ok().put("page", page);

    }

    @GetMapping(value = "/get/{orderSn}")
    public Result getOrderByOrderSn(@PathVariable(value = "orderSn") String orderSn) {

        Result result = new Result();
        try {

            OrderEntity entity = orderService.getOrderByOrderSn(orderSn);
            result.setCode(0);
            result.setMsg("成功.");
            if (entity == null) {

                result.setOrder(null);

            } else {

                OrderTo orderTo = new OrderTo();
                BeanUtils.copyProperties(entity, orderTo);
                result.setOrder(orderTo);

            }

            return result;

        } catch (Exception e) {

            e.printStackTrace();
            result.setCode(500);
            result.setMsg("服务出现异常.");
            result.setOrder(null);
            return result;

        }

    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OrderEntity order) {
        orderService.updateById(order);

        return R.ok();
    }

    @PostMapping(value = "/post/update/order/status")
    public R updateOrderStatus(String orderSn, String status) {

        System.out.println("updateOrderStatus method start...");
        return orderService.updateOrderStatus(orderSn,status);

    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
