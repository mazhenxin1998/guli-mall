package com.mzx.gulimall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.mzx.gulimall.order.dao.OrderItemDao;
import com.mzx.gulimall.order.dao.TestDao;
import com.mzx.gulimall.order.entity.OrderEntity;
import com.mzx.gulimall.order.entity.OrderItemEntity;
import com.mzx.gulimall.order.entity.Test;
import com.mzx.gulimall.order.exception.TransactionalException;
import com.mzx.gulimall.order.exception.TransactionalExceptionParent;
import com.mzx.gulimall.order.feign.CartServiceFeign;
import com.mzx.gulimall.order.feign.MemberServiceFeign;
import com.mzx.gulimall.order.feign.ProductServiceFeign;
import com.mzx.gulimall.order.feign.WareServiceFeign;
import com.mzx.gulimall.order.interceptor.OrderInterceptor;
import com.mzx.gulimall.order.service.OrderService;
import com.mzx.gulimall.order.vo.*;
import com.mzx.gulimall.util.CurrentStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
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

    /**
     * ThreadLocal每个线程都会在自己的工作内存中保留一份,起到了线程隔离作用.
     */
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
    private OrderDao orderDao;

    @Autowired
    private TestDao testDao;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    @Qualifier(value = "threadPoolExecutor")
    private ThreadPoolExecutor executor;

    private int count = 0;

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
    public String submit(OrderSubmitVo param, HttpServletRequest request, Model model) {

        // TODO: 2020/10/8 该出保证订单幂等性最后使用分布式锁来实现一下.
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        UserInfoTo userInfoTo = OrderInterceptor.THREAD_LOCAL.get();
        if (userInfoTo.getUserId() == null || userInfoTo.getUserId() <= 0) {

            return "toLogin";

        }

        MemberResultVo user = (MemberResultVo) request.getSession().getAttribute("user");
        username.set(user.getUsername());
        String token = param.getOrderToken();
        String tokenKey = CurrentStringUtils.append(new StringBuilder(), RedisConstant.TOKEN_UUID,
                userInfoTo.getUserId().toString());
        System.out.println("当前是第" + (++count) + "次通过同一个token" + token + "进行下单.");
        String command = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 " +
                "end";
        System.out.println("第" + count + "次访问token结束.");
        //  原子验证令牌通过lua脚本，并且需要先删除在进行验证.
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<Long>(command, Long.class),
                Arrays.asList(tokenKey), token);
        // 0-原子验证失败  1-原子验证成功.
        if (result == 1) {

            System.out.println("第" + count + "次下单成功.");
            OrderCreateTo orderCreateTo = this.createOrder(param, attributes);
            // TODO: 2020/10/8 如果this.save方法中出现异常,那么当前方法的事务捕获器是能捕获到this.save方法中的异常的.
            boolean success = this.save(orderCreateTo);
            if (success) {

                boolean lock = this.lockStock(orderCreateTo);
                if (lock) {

                    // TODO: 2020/10/9 锁定库存之后,还需要删除购物车中的内容.
                    model.addAttribute("orders", orderCreateTo);
                    return "topay";

                }

            }

        }

        System.out.println("第" + count + "次下单失败.");
        return "redirect:http://localhost:26000/toTrade.html";

    }

    /**
     * 远程查询库存服务,进行Sku订单的锁定.
     *
     * @param orderCreateTo
     * @return
     */
    private boolean lockStock(OrderCreateTo orderCreateTo) {

        // 远程请求.
        WareSkuLockVo wareSkuLockVo = new WareSkuLockVo();
        wareSkuLockVo.setOrderSn(orderCreateTo.getOrder().getOrderSn());
        LinkedList<WareSkuLockVo.Item> items = new LinkedList<>();
        orderCreateTo.getOrderItems().forEach(item -> {

            WareSkuLockVo.Item o = new WareSkuLockVo.Item();
            o.setSkuId(item.getSkuId());
            o.setNum(item.getSkuQuantity());
            // 这个Name其实设置不设置我感觉都一样.
            o.setTitle(item.getSpuName());
            items.add(o);

        });
        wareSkuLockVo.setLocks(items);
        // 为什么我数据传输不过去呢？ 这个wareSkuLockVo是有值的啊.
        R r = wareServiceFeign.lockStock(wareSkuLockVo);
        // 这个data是空值.
        Object data = r.get("data");
        boolean flag = true;
        // 可能这个data值获取不到.
        if (!StringUtils.isEmpty(data)) {

            List<Item> itemList = JSONObject.parseArray(data.toString(), Item.class);
            for (Item item : itemList) {

                // 只要发现有一个库存锁定没有锁定成功, 那么就break.
                if (!item.getLocked()) {

                    flag = false;
                    // break将会推出for循环.
                    break;

                }

            }

        }

        return flag;

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

        // 肯定不能循环增加啊.
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        OrderItemDao mapper = sqlSession.getMapper(OrderItemDao.class);
        for (OrderItemEntity orderItem : orderItems) {

            mapper.insert(orderItem);

        }

        // 执行SQL语句提交.
        sqlSession.commit();

    }

    /**
     * 保存订单.
     *
     * @param order 要保存的订单.
     */
    private void saveOrder(OrderEntity order) {

        orderDao.insert(order);
        System.out.println("向数据库增加Order成功.");

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
        OrderEntity order = new OrderEntity();
        order.init();
        UserInfoTo userInfoTo = OrderInterceptor.THREAD_LOCAL.get();
        order.setMemberId(userInfoTo.getUserId());
        order.setMemberUsername(username.get());
        String orderSn = IdWorker.getTimeId();
        OrderSn.set(orderSn);
        order.setOrderSn(orderSn);
        // 还需要为每一个OrderItem设置其所属的订单号.
        // 远程查询获取收获地址信息.
        CompletableFuture receiveFuture = buildReceiverInfo(param, order, attributes);
        // 远程查询购物车设置订单项,并且设置相关金额.
        CompletableFuture buildItemsFuture = buildOrderItem(orderCreateTo, attributes, orderSn);
        try {

            // 同步等待其他线程执行任务完毕.
            CompletableFuture.allOf(receiveFuture, buildItemsFuture).get();
            // 同步结束之后,应该在这里对订单总额和应付总额.payAmount表示应付金额.total_amount表示订单总额.
            // Order订单中的Payment应该表示的就是当前订单要支付的选项.
            order.setTotalAmount(orderCreateTo.getPayPrice());
            // payAmount的值应该是将订单总额减去运费还有优惠券减去的金额,但是由于在前面没有整合, 所以这里的订单总额和应付总额是相等.
            order.setPayAmount(orderCreateTo.getPayPrice());
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
    private CompletableFuture buildOrderItem(OrderCreateTo orderCreateTo, RequestAttributes attributes,
                                             String orderSn) {

        return CompletableFuture.runAsync(() -> {

            // 解决异步丢失请求问题.
            RequestContextHolder.setRequestAttributes(attributes);
            // 现在需要通过该List来获取到当前需要支付的总价格.
            List<CartItem> items = this.getCartItems();
            // 需要将cartItem转换成OrderItemEntity.
            // 需要将下面的lIst存放在OrderCreateVo中.
            List<OrderItemEntity> orderItemEntities = items.stream().map(item -> {

                return getOrderItem(item,orderSn);

            }).collect(Collectors.toList());
            orderCreateTo.setOrderItems(orderItemEntities);
            BigDecimal total = new BigDecimal(0);
            for (CartItem item : items) {

                total = total.add(item.getTotalPrice());

            }

            // 设置实际支付的金额,该金额可能会和提交上来的价格会有所变动.
            orderCreateTo.setPayPrice(total);

        }, executor);
    }

    private OrderItemEntity getOrderItem(CartItem item, String orderSn) {

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        // 为每一个OrderItemEntity设置其相关属性.
        orderItemEntity.setOrderSn(orderSn);
        orderItemEntity.setSkuId(item.getSkuId());
        orderItemEntity.setSkuName(item.getTitle());
        orderItemEntity.setSkuPic(item.getImage());
        orderItemEntity.setSkuPrice(item.getPrice());
        orderItemEntity.setSkuQuantity(item.getCount());
        // 将String数组转换成一个字符串.
        String attrs = StringUtils.arrayToDelimitedString(item.getSkuAttr().toArray(), ";");
        orderItemEntity.setSkuAttrsVals(attrs);
        // 现在需要根据SKU查询出其属于哪一个SPU.
        String spuInfoVoJsonString = productServiceFeign.getSpuInfoEntityBySkuId(item.getSkuId());
        SpuInfoVo spuInfoVo = JSON.parseObject(spuInfoVoJsonString, SpuInfoVo.class);
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

    /**
     * 测试事务的方法.
     * 注解中指定的rollbackFor到底管用吗?
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = TransactionalExceptionParent.class, propagation = Propagation.REQUIRED)
    public String testTransactional() {

        Test test = new Test(1L, "111");
        testDao.insert(test);
        // 在这里通过AOP暴露出来的代理对象进行当前对象的方法调用.
        // 在这里可以放心的强制类型转换.
        // 如果这样的话, 就造成了当前方法执行的对象和o这个对象不是一个.
        // 如果第二个方法的数据还能插进去, 那么就说明这个坑是真的.
        return "x  ";

    }

    /**
     * 既然是为测试SpringBoot中在使用Transactional的坑, 那么在第二个事务中传播行为就设置为REQUIRED.
     */
    @Transactional(rollbackFor = TransactionalException.class, propagation = Propagation.REQUIRES_NEW)
    public void transactional2() {

        Test test = new Test(2L, "222");
        testDao.insert(test);
        // 假如当前事务执行过程中发生了异常.
        // 如果一个对象的事务方法调用当前对象的事务方法的话，第二个事务不起作用的话, 那么下面抛出的这个异常件不会起作用.
        // 这个事务回滚了.... 这个是什么操作. ?
//        throw new TransactionalException("我是被调用方法的异常发生了. ");

    }

}