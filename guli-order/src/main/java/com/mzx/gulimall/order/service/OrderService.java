package com.mzx.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.order.OrderTo;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.order.entity.OrderEntity;
import com.mzx.gulimall.order.vo.OrderSubmitVo;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 订单
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 16:44:36
 */
public interface OrderService extends IService<OrderEntity> {

    /**
     * 查询案例.
     *
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 订单数据提交并且生成订单.
     * <p>
     * 使用RabbitMQ保证订单的最终数据一致性.
     * 1)、 远程锁定库存成功,但是订单这一块在远程查询后面出现了异常. 这个时候库存需要进行回滚.
     *
     * @param param
     * @param model
     * @param request 当前请求.
     * @return
     */
    String submit(OrderSubmitVo param, HttpServletRequest request, Model model);

    String testTransactional();

    /**
     * 根据订单号获取订单.
     *
     * @param orderSn
     * @return
     */
    OrderEntity getOrderByOrderSn(String orderSn);

    /**
     * 修改订单的状态.
     *
     * @param orderTo
     */
    void updateOrderStatusToClose(OrderTo orderTo);
}

