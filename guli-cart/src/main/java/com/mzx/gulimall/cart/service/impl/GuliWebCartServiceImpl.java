package com.mzx.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.cart.constant.StringConstant;
import com.mzx.gulimall.cart.feign.ProductServiceFeign;
import com.mzx.gulimall.cart.interceptor.CartInterceptor;
import com.mzx.gulimall.cart.service.GuliWebCartService;
import com.mzx.gulimall.cart.vo.CartParamVo;
import com.mzx.gulimall.cart.vo.UserInfoTo;
import com.mzx.gulimall.common.model.Cart;
import com.mzx.gulimall.common.model.CartItem;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.util.CurrentStringUtils;
import com.mzx.gulimall.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ZhenXinMa
 * @date 2020/9/5 17:29
 */
@Slf4j
@Service
public class GuliWebCartServiceImpl implements GuliWebCartService {

    @Autowired
    @Qualifier(value = "threadPoolExecutor")
    private ThreadPoolExecutor executor;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProductServiceFeign productServiceFeign;


    /**
     * 这里对Model的修改会映射到Controller中.
     *
     * @param request
     * @param model
     * @param isLogin 表示当前是否登录: true表示登录状态.
     */
    @Override
    public Cart cart(HttpServletRequest request, Model model, boolean isLogin) {

        if (isLogin) {

            return this.loginTrue(request, model);

        } else {

            return this.loginFalse(request, model);

        }

    }

    /**
     * 目前增加还有一个BUG还有一个问题能解决.
     * <p>
     * 当更新商品数量的时候出现错误.
     *
     * @param request
     * @param param
     * @param model
     * @return
     */
    @Override
    public CartItem add(HttpServletRequest request, CartParamVo param, Model model) {

        // 应该先判断当前Redis是否存在现在增加的商品, 如果存在那么就应该是修改其数量.
        boolean b = this.isUpdateOrInsert(request, param);
        // 如果返回true,那么就说明是增加.
        if (b) {

            CartItem item = this.getCartItem(param);
            // 只要代码执行到这里,就说明两个异步任务都已经完成.
            this.addCartToRedis(item, param.getSkuId());
            return item;

        } else {

            // 这里应该是Redis中存在的,所以说我要更新其数量.
            // 传的参数: skuId key item
            // 更新.
            CartItem item = this.getCartItem(param);
            // 应该将更新之后的CartItem返回.
            return this.updateRedisCartItem(item);

        }

    }

    private CartItem updateRedisCartItem(CartItem item) {

        // 进行更新. 仍然需要判断是离线状态还是在线状态.
        UserInfoTo userInfoTo = CartInterceptor.local.get();
        if (StringUtils.isEmpty(userInfoTo.getUserId())) {

            // 说明当前用户没有进行登录.
            BoundHashOperations<String, Object, Object> ops = this.getBoundHash(userInfoTo.getUserKey());
            // 这里更新采用的是直接覆盖.
            // 存储的是JSON.
            // Long类型不能转换为String类型. 需要使用String.valueof
            Object o = ops.get(String.valueOf(item.getSkuId()));
            // 这里应该是不用做判断的,因为只要是代码能执行到了这里,就说明判断是没有必要的.
            CartItem cartItem = JSON.parseObject(o.toString(), CartItem.class);
            // 仅仅需要修改数量即可.
            cartItem.setCount(cartItem.getCount() + 1);
            // 再次将其转换成JSON存入Redis中.
            String jsonString = JSON.toJSONString(cartItem);
            HashMap<String, String> map = new HashMap<>();
            map.put(cartItem.getSkuId().toString(), jsonString);
            ops.putAll(map);
            return cartItem;

        } else {

            // 说明当前用户进行了登录.
            BoundHashOperations<String, Object, Object> ops = this.getBoundHash(userInfoTo.getUserId().toString());
            Object o = ops.get(String.valueOf(item.getSkuId()));
            CartItem cartItem = JSON.parseObject(o.toString(), CartItem.class);
            // 修改其数量即可.
            cartItem.setCount(cartItem.getCount() + 1);
            String jsonString = JSON.toJSONString(cartItem);
            HashMap<String, String> map = new HashMap<>();
            map.put(cartItem.getSkuId().toString(), jsonString);
            ops.putAll(map);
            return cartItem;

        }

    }

    private boolean isUpdateOrInsert(HttpServletRequest request, CartParamVo param) {

        // 判断当前是增加还是更新数量.
        UserInfoTo userInfoTo = CartInterceptor.local.get();
        BoundHashOperations<String, Object, Object> ops = null;
        if (StringUtils.isEmpty(userInfoTo.getUserId())) {

            // 当前状态是用户没有进行登录状态.
            ops = this.getBoundHash(userInfoTo.getUserKey());
//            ops = stringRedisTemplate.boundHashOps("cart:" + userInfoTo.getUserKey());
            // 获取数据出现错误.
            // 这里查询出来的数据可能为空.
            // 一查询就直接报错.
            // Long类型不能转换成String类型.
            Object o = ops.get(String.valueOf(param.getSkuId()));
            // o可能为空.
            if (StringUtils.isEmpty(o)) {

                // 如果o是空的,那么就说明Redis中没有存在该商品.
                return true;

            } else {

                return false;

            }

        } else {

            // 当前状态是用户已经进行登录状态了.
            ops = this.getBoundHash(userInfoTo.getUserId().toString());
            // ops = stringRedisTemplate.boundHashOps("cart:" + userInfoTo.getUserId());
            Object o = ops.get(String.valueOf(param.getSkuId()));
            if (StringUtils.isEmpty(o)) {

                // 表示当前Redis中没有该商品,那么就返回ture.
                return true;

            } else {

                return false;

            }

        }

    }

    /**
     * 通过查询参数封装出一个CartItem对象.
     *
     * @param param
     * @return
     */
    private CartItem getCartItem(CartParamVo param) {

        CartItem item = new CartItem();
        // 销售属性和SKU的基本属性可以并行查询.
        CompletableFuture<Void> infoFuture = CompletableFuture.runAsync(() -> {

            // 因为不需要返回值.
            R info = productServiceFeign.info(param.getSkuId());
            Map<String, Object> o = (Map<String, Object>) info.get("skuInfo");
            String defaultImg = (String) o.get("skuDefaultImg");
            String skuTitle = (String) o.get("skuTitle");
            // double不能转换成String.
            Double price = (Double) o.get("price");
            item.setSkuId(param.getSkuId());
            item.setCount(param.getCount());
            item.setPrice(new BigDecimal(price));
            item.setImage(defaultImg);
            item.setTitle(skuTitle);
            // 进行计算总价格.
            item.getTotalPrice();

        }, executor);

        CompletableFuture<Void> attrFuture = CompletableFuture.runAsync(() -> {

            // 对销售属性进行封装
            // TODO: 学习该SQL中方法 group_concat的使用.
            R saleAttr = productServiceFeign.findSkuSaleAttr(param.getSkuId());
            Object size = saleAttr.get("size");
            if ("1".equals(size)) {

                // 表示当前item的销售属性仅仅只有一行.
                // 这个是单个字符串.
                String attr = saleAttr.get("attr").toString();
                ArrayList<String> list = new ArrayList<>();
                list.add(attr);
                item.setSkuAttr(list);

            } else {

                // 表示有多个销售属性.
                // 目前用不到吧?
                // TODO: 返回的SKU的销售属性是多个,这个时候放入的就是一个list.
                // 返回的是一个数组.
                // TODO: 这里不知道有没有错误.
                List<String> attr = (List<String>) saleAttr.get("attr");
                item.setSkuAttr(attr);

            }

        }, executor);

        // 只有上面两个异步任务都完成之后,
        try {

            CompletableFuture.allOf(infoFuture, attrFuture).get();

        } catch (InterruptedException e) {

            System.out.println(e);

        } catch (ExecutionException e) {

            System.out.println(e);
        }

        return item;
    }

    /**
     * 针对用户登录状态进行查询.
     *
     * @param request
     * @param model
     */
    private Cart loginTrue(HttpServletRequest request, Model model) {

        return null;

    }

    /**
     * 针对用户未登录状态进行查询.
     *
     * @param request
     * @param model
     */
    private Cart loginFalse(HttpServletRequest request, Model model) {

        // 现在是用户未进行登录状态的.
        // 所以使用user-key来进行查询.
        UserInfoTo userInfoTo = CartInterceptor.local.get();
        // 现在要将该hashKey下的所有map查询出来.
//        ops.multiGet()
        // 现在是怎么进行查询.
        // 现在需要将其封装成一个Cart.
        Cart cart = new Cart();
        List<CartItem> cartItemList = this.getAllCartItem(userInfoTo.getUserKey());
        int count = 0;
        //  计算所有个数.
        // 顺便计算总价格.
        BigDecimal bigDecimal = new BigDecimal(0);
        for (CartItem item : cartItemList) {

            count += item.getCount();
            bigDecimal = bigDecimal.add(item.getTotalPrice());
        }

        System.out.println(bigDecimal+"------");
        // 输出总价格.
        // 现在我需要对其求和.

        cart.setItems(cartItemList);
        // 这个count需要的是所有商品的count来
        // 这个值需要计算出来.
        cart.setCount(count);
        // 这个count设置的是上面的size.
        cart.setCountType(cartItemList.size());
        // 调用一下总价的get方法,来进行set.
        // 看一下最后输出的总价.
        // TODO: 现在是总价计算不出来.
        cart.getTotalPrice();
        cart.setTotalPrice(bigDecimal);
        return cart;

    }

    private BoundHashOperations<String, Object, Object> getBoundHash(String key) {

        // 这里使用之后 应该立马进行对其进行移除: 因为这里是最后一次使用这个ThreadLocal获取当前用户的信息.
        // TODO: 考虑:这里可以进行删除吗？
        // Tomcat是一个请求一个线程: 所以说这里是可以进行清除的.
        UserInfoTo userInfoTo = CartInterceptor.local.get();
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(
                CurrentStringUtils.append(new StringBuilder(), StringConstant.CART_PREFIX, key));
        return ops;

    }

    private void addCartToRedis(CartItem item, Long skuId) {

        // 增加之前应该判断是离线状态还是用户已经登录状态.
        UserInfoTo userInfoTo = CartInterceptor.local.get();
        if (StringUtils.isEmpty(userInfoTo.getUserId())) {

            // 说明现在用户是未登录状态
            // 那么传进去的key就是用户的user-key.
            this.addCartToRedis(item, userInfoTo.getUserKey(), skuId);

        } else {

            // 现在用户是登录状态.
            // 那么传进去的key是用户的ID.
            this.addCartToRedis(item, userInfoTo.getUserId().toString(), skuId);

        }

    }


    /**
     * 第二个参数key和cart: 一起组成一个hash来表示键值。
     *
     * @param item
     * @param key
     * @param skuId
     */
    private void addCartToRedis(CartItem item, String key, Long skuId) {

        BoundHashOperations<String, Object, Object> ops = this.getBoundHash(key);
        String jsonString = JSON.toJSONString(item);
        // 这里直接使用ops进行put操作.
        // TODO: 向Redis中增加商品失败.
        // 这里为什么会失败啊?
        // 现在情况是如果想Redis中添加Hash类型数据,那么就会报错.
        HashMap<String, String> map = new HashMap<>();
        map.put(skuId.toString(), jsonString);
        // 使用 ops对其进行操作.
        ops.putAll(map);
        // 购物车需要设置有效时间吗?  设置一个月的吧
        // 设置过期时间为一个月.
        // 这里的键是那个hash键.
        // TODO:  这个键一定是正确的吗？
        // 一定要保证这个key一定是正确的.
        stringRedisTemplate.expire(CurrentStringUtils.append(new StringBuilder(), StringConstant.CART_PREFIX, key),
                DateTimeUtils.getOneMonthTimeSeconds(), TimeUnit.SECONDS);
        log.info("增加购物车成功: {}", jsonString);

    }


    private List<CartItem> getAllCartItem(@NotNull String key) {

        HashOperations<String, Object, Object> ops = stringRedisTemplate.opsForHash();
        String hk = CurrentStringUtils.append(new StringBuilder(), StringConstant.CART_PREFIX, key);
        // 现在要将该hashKey下的所有map查询出来.
        // 现在需要将其封装成一个Cart.
        List<Object> values = ops.values(hk);
        // 参数是hashKey的一个集合.
        Integer count = 0;
        List<CartItem> cartItemList = values.stream().map(item -> {

            // 在这里需要将item转换成一个CartItem
            CartItem cartItem = JSON.parseObject(item.toString(), CartItem.class);
            // 累加所有count.
            return cartItem;

        }).collect(Collectors.toList());
        return cartItemList;

    }


}
