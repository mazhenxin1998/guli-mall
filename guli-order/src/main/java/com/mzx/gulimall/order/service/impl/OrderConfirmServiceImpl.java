package com.mzx.gulimall.order.service.impl;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
        if (userInfoTo.getUserId() == null || userInfoTo.getUserId() <= 0) {

            // 用户没有登录状态.
            return null;

        } else {

            R addr = memberServiceFeign.getAddr(userInfoTo.getUserId());
            List<MemberAddressVo> listAddr = (List<MemberAddressVo>) addr.get("data");
            confirmVo.setAddress(listAddr);
            R checkedCartItems = cartServiceFeign.getCheckedCartItems();
            // getData返回的应该是一个LinkedListMap
            List<Map<String, Object>> items = (List<Map<String, Object>>) checkedCartItems.get("data");
            List<OrderItemVo> itemVos = items.stream().map(item -> {

                OrderItemVo itemVo = new OrderItemVo();
                itemVo.setSkuId(Long.valueOf(item.get("skuId").toString()));
                itemVo.setTitle(item.get("title").toString());
                itemVo.setImage(item.get("image").toString());
                itemVo.setCount(Integer.valueOf(item.get("count").toString()));
                itemVo.setPrice(new BigDecimal(item.get("price").toString()));
                List<String> list = (List<String>) item.get("skuAttr");
                itemVo.setSkuAttr(list);
                // 给attr进行赋值.
                itemVo.setTotalPrice(new BigDecimal(item.get("totalPrice").toString()));
                return itemVo;

            }).collect(Collectors.toList());
            confirmVo.setItems(itemVos);
            return confirmVo;

        }

    }

    @Override
    public OrderConfirmVo queryOrderConfirmSyn() {

        // 用户能执行到这里就说明一定是登录状态的.
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        UserInfoTo userInfoTo = OrderInterceptor.THREAD_LOCAL.get();
        if (userInfoTo.getUserId() == null || userInfoTo.getUserId() <= 0) {

            // TODO: 2020/9/27 这种情况应该是不会出现的.
            return null;

        }

        // Java中是值传递,而confirmVo是一个对象的引用,其在方法之间传递,传递的是confirmVo对象地址的值,方法中对confirmVo做的修改能同步到当前.
        // 方法中.
        this.remoteQuery(confirmVo, attributes, userInfoTo);
        // 优惠额度暂时没有写.
        confirmVo.setIntegration(0);
        confirmVo.setTotal();
        return confirmVo;

    }

    private void remoteQuery(OrderConfirmVo confirmVo, ServletRequestAttributes attributes,
                             UserInfoTo userInfoTo) {

        long start = System.currentTimeMillis();
        // 1. 远程查询订单确认页面需要的收货地址.
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {

            // 解决Feign异步丢失请求头的问题.
            // 在当前线程中进行ThreadLocal的重新设置,这样每个异步请求线程中的ThreadLocal都会有一个RequestContextHolder中的一副本.
            RequestContextHolder.setRequestAttributes(attributes);
            R addr = memberServiceFeign.getAddr(userInfoTo.getUserId());
            List<MemberAddressVo> listAddr = (List<MemberAddressVo>) addr.get("data");
            confirmVo.setAddress(listAddr);

        }, executor);
        CompletableFuture<Void> itemFuture = CompletableFuture.runAsync(() -> {

            RequestContextHolder.setRequestAttributes(attributes);
            // 远程查询购物车中选中的数据.
            R checkedCartItems = cartServiceFeign.getCheckedCartItems();
            // getData返回的应该是一个LinkedListMap
            List<Map<String, Object>> items = (List<Map<String, Object>>) checkedCartItems.get("data");
            List<OrderItemVo> itemVos = items.stream().map(item -> {

                // TODO: 2020/9/27 思考为什么异步线程能访问this.convert(Map)方法?
                /*
                 * ？？？？
                 * */
                return this.convert(item);

            }).collect(Collectors.toList());
            confirmVo.setItems(itemVos);

        }, executor);

        // 异步查询库存服务.


        try {

            // 只要上面两个异步任务完成,那么当前方法就执行完毕.
            CompletableFuture.allOf(addressFuture, itemFuture).get();
            long end = System.currentTimeMillis();
            System.out.println("两次远程调用花费时间: " + (end - start) + "毫秒. ");

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

    }

    private OrderItemVo convert(Map<String, Object> item) {

        OrderItemVo itemVo = new OrderItemVo();
        itemVo.setSkuId(Long.valueOf(item.get("skuId").toString()));
        itemVo.setTitle(item.get("title").toString());
        itemVo.setImage(item.get("image").toString());
        itemVo.setCount(Integer.valueOf(item.get("count").toString()));
        itemVo.setPrice(new BigDecimal(item.get("price").toString()));
        List<String> list = (List<String>) item.get("skuAttr");
        itemVo.setSkuAttr(list);
        itemVo.setTotalPrice(new BigDecimal(item.get("totalPrice").toString()));
        return itemVo;

    }

    /**
     * 转换.
     *
     * @param addressVo
     * @param item
     */
    private void adapter(MemberAddressVo addressVo, MemberAddressVo item) {

        addressVo.setRegion(item.getRegion());
        addressVo.setProvince(item.getProvince());
        if (item.getPostCode() != null) {

            addressVo.setPostCode(item.getPostCode());

        }

        addressVo.setPhone(item.getPhone());
        addressVo.setName(item.getPhone());
        addressVo.setMemberId(item.getMemberId());
        addressVo.setId(item.getId());
        addressVo.setDetailAddress(item.getDetailAddress());
        addressVo.setDefaultStatus(item.getDefaultStatus());
        addressVo.setCity(item.getCity());
        addressVo.setAreacode(item.getAreacode());

    }

}
