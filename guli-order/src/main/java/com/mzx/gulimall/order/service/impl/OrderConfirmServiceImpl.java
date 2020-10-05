package com.mzx.gulimall.order.service.impl;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.constant.RedisConstant;
import com.mzx.gulimall.order.feign.CartServiceFeign;
import com.mzx.gulimall.order.feign.MemberServiceFeign;
import com.mzx.gulimall.order.feign.WareServiceFeign;
import com.mzx.gulimall.order.interceptor.OrderInterceptor;
import com.mzx.gulimall.order.service.IOrderConfirmService;
import com.mzx.gulimall.order.vo.*;
import com.mzx.gulimall.util.CurrentStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
    private WareServiceFeign wareServiceFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

        OrderConfirmVo confirmVo = new OrderConfirmVo();
        // 当前请求(从浏览器中发送的请求.)
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        UserInfoTo userInfoTo = OrderInterceptor.THREAD_LOCAL.get();
        // 避免用户未登录状态.
        if (userInfoTo.getUserId() == null || userInfoTo.getUserId() <= 0) {

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

    @Override
    public String order(HttpServletRequest request, Model model) {

        OrderConfirmVo confirmVo = this.queryOrderConfirmSyn();
        if (confirmVo == null) {

            String originUrl = "http://localhost:26000" + request.getRequestURI();
            return "redirect:http://localhost:24000/oauth/login.html?origin_url=" + originUrl;

        }

        // TODO: 2020/9/30 这个uuid需要保存到redis中, 如果在向Redis中存入数据的时候,如果存入失败的时候怎么办？
        // TODO: 2020/10/2 如果存入失败的话,那么就提示用户页面生成失败.
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        if (this.saveToken(uuid)) {

            // 保存成功.
            confirmVo.setToken(uuid);
            model.addAttribute("confirm", confirmVo);
            return "order";

        }else{

            // 返回到那个页面呢？
            // 重新返回到购物车?
            // 但是怎么提示用户UUID生成失败呢?
            // 之后在说吧.
            return "redirect:http://localhost:25000/cart/cartlist.html";

        }

    }

    private void remoteQuery(OrderConfirmVo confirmVo, ServletRequestAttributes attributes,
                             UserInfoTo userInfoTo) {

        long start = System.currentTimeMillis();
        // 远程查询住址.
        CompletableFuture<Void> addressFuture = this.getAddressFuture(confirmVo, attributes, userInfoTo);
        // 远程查询购物车和库存服务.
        CompletableFuture<List<Long>> itemFuture = this.getItemFuture(confirmVo, attributes, userInfoTo);
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

    /**
     * 远程查询订单确认页面需要的收货地址.
     *
     * @param confirmVo
     * @param attributes
     * @param userInfoTo
     * @return
     */
    private CompletableFuture<Void> getAddressFuture(OrderConfirmVo confirmVo, ServletRequestAttributes attributes,
                                                     UserInfoTo userInfoTo) {

        return CompletableFuture.runAsync(() -> {

            // 解决Feign异步丢失请求头的问题.
            // 在当前线程中进行ThreadLocal的重新设置,这样每个异步请求线程中的ThreadLocal都会有一个RequestContextHolder中的一副本.
            RequestContextHolder.setRequestAttributes(attributes);
            R addr = memberServiceFeign.getAddr(userInfoTo.getUserId());
            List<MemberAddressVo> listAddr = (List<MemberAddressVo>) addr.get("data");
            confirmVo.setAddress(listAddr);

        }, executor);

    }

    /**
     * @param confirmVo
     * @param attributes
     * @param userInfoTo
     * @return
     */
    private CompletableFuture<List<Long>> getItemFuture(OrderConfirmVo confirmVo, ServletRequestAttributes attributes
            , UserInfoTo userInfoTo) {

        return CompletableFuture.supplyAsync(() -> {

            // 现在是都进行设置了,但是为什么就是会有以及出现错误.
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
            List<Long> ids = itemVos.stream().map(OrderItemVo::getSkuId).collect(Collectors.toList());
            return ids;

        }, executor).whenCompleteAsync((data, e) -> {

            // 通过上一个异步线程执行的结果对每个Sku的查询库存.
            if (e != null) {

                // 有异常发生了,终止往下执行.
                throw new RuntimeException("远程查询SKU当前剩余库存出现异常: " + e.getMessage());

            } else {

                this.setItemStock(data, confirmVo, attributes);

            }

        }, executor);

    }

    /**
     * 通过ids查出每一个SKU的库存数据,并且进行封装.
     * <p>
     * 直接进行远程查询就行,不需要再开启一个线程了. 因为当前就在一个空闲的线程中.
     *
     * @param ids       当前用户需要购买的所有商品的skus.
     * @param confirmVo 当前需要返回的vo.
     */
    private void setItemStock(List<Long> ids, OrderConfirmVo confirmVo,
                              ServletRequestAttributes attributes) {

        // 当前线程也需要设置环境上下文.
        RequestContextHolder.setRequestAttributes(attributes);
        // 如果当前服务查询出错了怎么办.
        String[] params = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {

            params[i] = String.valueOf(ids.get(i));

        }

        R r = wareServiceFeign.getListStock(params);
        // 强制类型转换应该是没有问题的.
        if (StringUtils.isEmpty(r.get("data"))) {

            confirmVo.getItems().forEach(item -> {

                // 设置默认值.
                item.setRepertory(0);

            });

        } else {

            // List中的每一个都是一个Map.
            List<LinkedHashMap<String, Object>> tos = (List<LinkedHashMap<String, Object>>) r.get("data");
            List<SkuWareStockTo> wareStock = this.getWareStock(tos);
            confirmVo.getItems().forEach(item -> {

                for (SkuWareStockTo skuWareStockTo : wareStock) {

                    // 现在是不知道这里会不会出现错误.
                    if (skuWareStockTo.getSkuId().equals(item.getSkuId())) {

                        // 现在需要将Long类型转换为Integer类型.
                        item.setRepertory(Integer.valueOf(String.valueOf(skuWareStockTo.getStock())));

                    }

                }

            });

        }

    }

    private List<SkuWareStockTo> getWareStock(List<LinkedHashMap<String, Object>> tos) {

        List<SkuWareStockTo> wares = new ArrayList<>();
        tos.forEach(item -> {

            Long stock = Long.valueOf(item.get("stock").toString());
            Long wareId = Long.valueOf(item.get("wareId").toString());
            Long skuId = Long.valueOf(item.get("skuId").toString());
            SkuWareStockTo ware = new SkuWareStockTo(stock, wareId, skuId);
            wares.add(ware);

        });
        return wares;

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

    /**
     * 向Redis中存入当前页面生成的Token.
     * <p>
     * 在Key中添加用户ID是为了Redis快速进行查找？
     * Redis中token保存的形式: GULI:TOKEN:userId:UUID.
     *
     * @param token
     * @return
     */
    private boolean saveToken(String token) {

        UserInfoTo userInfoTo = OrderInterceptor.THREAD_LOCAL.get();
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        try {

            // GULI:TOKEN:ID = UUID
            ops.set(CurrentStringUtils.append(new StringBuilder(), RedisConstant.TOKEN_UUID,
                    userInfoTo.getUserId().toString())
                    , token,30, TimeUnit.MINUTES);
            return true;

        } catch (Exception e) {

            // 应该将异常记录下.
            return false;

        }

    }

}
