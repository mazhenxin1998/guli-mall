package com.mzx.gulimall.order.service.impl;

import com.mzx.gulimall.common.model.CartItem;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.feign.CartServiceFeign;
import com.mzx.gulimall.order.feign.MemberServiceFeign;
import com.mzx.gulimall.order.interceptor.OrderInterceptor;
import com.mzx.gulimall.order.service.IOrderConfirmService;
import com.mzx.gulimall.order.vo.MemberAddressVo;
import com.mzx.gulimall.order.vo.OrderConfirmVo;
import com.mzx.gulimall.order.vo.OrderItemVo;
import com.mzx.gulimall.order.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 10:09 周六.
 */
@Service
public class OrderConfirmServiceImpl implements IOrderConfirmService {

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @Autowired
    private CartServiceFeign cartServiceFeign;

    @Autowired
    @Qualifier(value = "threadPoolExecutor")
    private ThreadPoolExecutor executor;

    @Override
    public OrderConfirmVo queryOrderConfirm() {

        OrderConfirmVo confirmVo = new OrderConfirmVo();
        // OrderInterceptor为什么是空啊?
        UserInfoTo userInfoTo = OrderInterceptor.THREAD_LOCAL.get();
        // TODO: 2020/9/26 通过Feign远程调用服务,如果路径参数上的值是null,将会触发feign的404错误.
        // TODO: 2020/9/26 下面的代码就已经触发了这个错误条件.明天修正.
        if (userInfoTo.getUserId() == null && userInfoTo.getUserId() <= 0) {

            // 用户没有登录状态.
            return null;

        }else{

            R addr = memberServiceFeign.getAddr(userInfoTo.getUserId());
            List<MemberAddressVo> listAddr = (List<MemberAddressVo>) addr.get("data");
            confirmVo.setAddress(listAddr);

            R checkedCartItems = cartServiceFeign.getCheckedCartItems();
            List<CartItem> items = (List<CartItem>) checkedCartItems.get("data");
            // feign远程调用将会出现请求头丢失问题.
            List<OrderItemVo> orderItemVos = items.stream().map(item -> {

                OrderItemVo orderItemVo = new OrderItemVo();
                orderItemVo.setSkuId(item.getSkuId());
                orderItemVo.setTitle(item.getTitle());
                orderItemVo.getTotalPrice();
                orderItemVo.setImage(item.getImage());
                orderItemVo.setPrice(item.getPrice());
                orderItemVo.setCount(item.getCount());
                return orderItemVo;

            }).collect(Collectors.toList());
            confirmVo.setItems(orderItemVos);

            return confirmVo;

        }

    }

    public OrderConfirmVo queryOrderConfirmSyn() {

        // 用户能执行到这里就说明一定是登录状态的.
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        UserInfoTo userInfoTo = OrderInterceptor.THREAD_LOCAL.get();
        // 1. 远程查询订单确认页面需要的收货地址.
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {

            R addr = memberServiceFeign.getAddr(userInfoTo.getUserId());
            List<MemberAddressVo> listAddr = (List<MemberAddressVo>) addr.get("data");
            confirmVo.setAddress(listAddr);

        }, executor);
        CompletableFuture<Void> itemFuture = CompletableFuture.runAsync(() -> {

            // 远程查询购物车中选中的数据.

        }, executor);


        return null;
    }
}
