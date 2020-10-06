package com.mzx.gulimall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.model.CartItem;
import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.order.constant.RedisConstant;
import com.mzx.gulimall.order.dao.OrderDao;
import com.mzx.gulimall.order.entity.OrderEntity;
import com.mzx.gulimall.order.entity.OrderItemEntity;
import com.mzx.gulimall.order.feign.CartServiceFeign;
import com.mzx.gulimall.order.feign.MemberServiceFeign;
import com.mzx.gulimall.order.feign.ProductServiceFeign;
import com.mzx.gulimall.order.feign.WareServiceFeign;
import com.mzx.gulimall.order.interceptor.OrderInterceptor;
import com.mzx.gulimall.order.service.OrderService;
import com.mzx.gulimall.order.vo.*;
import com.mzx.gulimall.util.CurrentStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;


/**
 * 订单服务.
 *
 * @author ZhenXinMa
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private ThreadLocal<String> username = new ThreadLocal<>();

    private ThreadLocal<String> OrderSn = new ThreadLocal<>();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WareServiceFeign wareServiceFeign;

    @Autowired
    private ProductServiceFeign productServiceFeign;

    @Autowired
    private CartServiceFeign cartServiceFeign;

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @Autowired
    @Qualifier(value = "threadPoolExecutor")
    private ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );
        return new PageUtils(page);

    }


    /**
     * 保证原子操作，可以使用lua脚本进行,也可以使用Redisson分布式锁来保证.
     * <p>
     * 如果要在当前对象的事务方法中调用当前对象的事务方法,那么就应该使用AopContext.currentProxy来获取到当前的代理对象,并且通过该方法来进行事务
     * 方法之间的互相调用.
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String submit(OrderSubmitVo param, HttpServletRequest request) {

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        // 先不管用户是否登录, 先保证当前接口的幂等.
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // GULI:TOKEN:ID:UUID.
        // 如果当前用户没有进行登录,那么直接返回false.
        UserInfoTo userInfoTo = OrderInterceptor.THREAD_LOCAL.get();
        if (userInfoTo.getUserId() == null || userInfoTo.getUserId() <= 0) {

            return "toLogin";

        }

        // 全局用户名.
        MemberResultVo user = (MemberResultVo) request.getSession().getAttribute("user");
        username.set(user.getUsername());
        String token = param.getOrderToken();
        String tokenKey = CurrentStringUtils.append(new StringBuilder(), RedisConstant.TOKEN_UUID,
                userInfoTo.getUserId().toString());
        String script =
                "if redis.call('get',KYES[1]) == ARGV[1] then return redis.call('del',KYES[2]) else return 0 end";
        //  原子验证令牌通过lua脚本，并且需要先删除在进行验证.
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Arrays.asList(tokenKey), token);
        // 0-原子验证失败  1-原子验证成功.
        if (result == 1) {

            // 业务逻辑正常处理.
            // 如果方法createOrder方法没有完成, 那么当前方法就会阻塞.
            OrderCreateTo orderCreateTo = this.createOrder(param, attributes);
            boolean success = this.save(orderCreateTo);
            if (success) {

                return "topay";

            }

        }

        // 还是订单确认页面.
        return "order";

    }

    /**
     * 将订单和订单项保存到服务中.
     * 当前方法需要在事务中进行.而在上面调用该方法的方法中已经起了事务.
     * <p>
     * 1. 分两个部分进行保存.
     * 1.1 保存订单.
     * 1.2 批量保存订单项.
     * 2. 保存订单的时候需要根据下订单的数量进行锁定库存.
     *
     * @param orderCreateTo
     * @return
     */
    private boolean save(OrderCreateTo orderCreateTo) {

        // TODO: 2020/10/6 订单数据构造成功.
        // 保存订单.
        OrderEntity order = orderCreateTo.getOrder();
        // 不需要自己捕获异常, 如果在增加的时候捕获到了异常, Spring会自捕捉到并且事务自动回滚.
        this.saveOrder(order);
        List<OrderItemEntity> orderItems = orderCreateTo.getOrderItems();
        this.saveOrderItems(orderItems);
        return true;

    }

    /**
     * 保存订单项.
     *
     * @param orderItems 要保存的订单项.
     */
    private void saveOrderItems(List<OrderItemEntity> orderItems) {

    }

    /**
     * 保存订单.
     *
     * @param order 要保存的订单.
     */
    private void saveOrder(OrderEntity order) {

    }

    /**
     * 创建订单.
     *
     * @param param
     * @param attributes 当前请求域中的所有数据包含了Cookie等信息.
     * @return
     */
    private OrderCreateTo createOrder(OrderSubmitVo param, RequestAttributes attributes) {

        OrderCreateTo orderCreateTo = new OrderCreateTo();
        // 订单ID默认使用数据库自增主键.
        // 异步进行操作.
        OrderEntity order = new OrderEntity();
        // 设置订单的初始状态.
        order.init();
        order.setMemberUsername(username.get());
        String orderSn = IdWorker.getTimeId();
        OrderSn.set(orderSn);
        order.setOrderSn(orderSn);
        // TODO: 2020/10/4 订单总额还没有进行设置.
        // 远程查询获取收获地址信息.
        CompletableFuture receiveFuture = buildReceiverInfo(param, order, attributes);
        // 设置金额相关. 需要重新从购物车中获取.
        CompletableFuture buildItemsFuture = buildOrderItem(orderCreateTo, attributes);
        try {

            // 同步等待其他线程执行任务完毕.
            CompletableFuture.allOf(receiveFuture, buildItemsFuture).get();
            // 设置OrdreCreateTo
            // TODO: 2020/10/4 当前还存在Feign远程调用丢失请求头问题.
            orderCreateTo.setOrder(order);

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        return orderCreateTo;

    }

    /**
     * 构建OrderItems.
     *
     * @return
     */
    private CompletableFuture buildOrderItem(OrderCreateTo orderCreateTo, RequestAttributes attributes) {

        return CompletableFuture.runAsync(() -> {

            // 解决异步丢失请求问题.
            RequestContextHolder.setRequestAttributes(attributes);
            // 现在需要通过该List来获取到当前需要支付的总价格.
            List<CartItem> items = this.getCartItems();
            // 需要将cartItem转换成OrderItemEntity.
            // 需要将下面的lIst存放在OrderCreateVo中.
            List<OrderItemEntity> orderItemEntities = items.stream().map(item -> {

                return getOrderItem(item);

            }).collect(Collectors.toList());
            orderCreateTo.setOrderItems(orderItemEntities);
            // TODO: 2020/10/5 还需要再次遍历下?
            BigDecimal total = new BigDecimal(0);
            for (CartItem item : items) {

                total = total.add(item.getTotalPrice());

            }

            // 设置实际支付的金额.
            // 该金额可能会和提交上来的价格会有所变动.
            orderCreateTo.setPayPrice(total);

        }, executor);
    }


    private OrderItemEntity getOrderItem(CartItem item) {

        // TODO: 2020/10/4 存在Feign远程请求丢失请求头的问题.

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        // 为每一个OrderItemEntity设置其相关属性.
        orderItemEntity.setOrderSn(OrderSn.get());
        orderItemEntity.setSkuId(item.getSkuId());
        orderItemEntity.setSkuName(item.getTitle());
        orderItemEntity.setSkuPic(item.getImage());
        orderItemEntity.setSkuPrice(item.getPrice());
        // 设置商品的数量.
        orderItemEntity.setSkuQuantity(item.getCount());
        String attrs = StringUtils.arrayToDelimitedString(item.getSkuAttr().toArray(), ";");
        orderItemEntity.setSkuAttrsVals(attrs);
        // 现在需要根据SKU查询出其属于哪一个SPU.
        SpuInfoVo spuInfoVo = productServiceFeign.getSpuInfoEntityBySkuId(item.getSkuId());
        orderItemEntity.setSpuId(spuInfoVo.getId());
        orderItemEntity.setSpuBrand(spuInfoVo.getBrandName());
        orderItemEntity.setSpuName(spuInfoVo.getSpuName());
        orderItemEntity.setCategoryId(spuInfoVo.getCatalogId());
        return orderItemEntity;

    }

    private CompletableFuture buildReceiverInfo(OrderSubmitVo param, OrderEntity order, RequestAttributes attributes) {

        return CompletableFuture.runAsync(() -> {

            // 解决Feign远程请求丢失请求头的问题.
            RequestContextHolder.setRequestAttributes(attributes);
            // 还得查询收人地址信息.
            MemberAddressVo address = memberServiceFeign.getById(param.getAddrId());
            order.setReceiverCity(address.getCity());
            order.setReceiverDetailAddress(address.getDetailAddress());
            order.setReceiverName(address.getName());
            order.setReceiverPhone(address.getPhone());
            order.setReceiverPostCode(address.getPostCode());
            order.setReceiverProvince(address.getProvince());
            order.setReceiverRegion(address.getRegion());

        }, executor);

    }


    private List<CartItem> getCartItems() {

        // TODO: 2020/10/4 该方法需要进行调试.
        R data = cartServiceFeign.getCheckedCartItems();
        List<LinkedHashMap<String, Object>> items = (List<LinkedHashMap<String, Object>>) data.get("data");
        return items.stream().map(item -> {

            CartItem cartItem = new CartItem();
            cartItem.setCheck(true);
            cartItem.setCount(Integer.valueOf(item.get("count").toString()));
            cartItem.setImage(item.get("image").toString());
            cartItem.setPrice(new BigDecimal(item.get("price").toString()));
            List<String> list = (List<String>) item.get("skuAttr");
            cartItem.setSkuAttr(list);
            cartItem.setSkuId(Long.valueOf(item.get("skuId").toString()));
            cartItem.setTitle(item.get("title").toString());
            cartItem.setTotalPrice(new BigDecimal(item.get("totalPrice").toString()));
            return cartItem;

        }).collect(Collectors.toList());

    }

}