package com.mzx.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.cart.constant.StringConstant;
import com.mzx.gulimall.cart.feign.ProductServiceFeign;
import com.mzx.gulimall.cart.interceptor.CartInterceptor;
import com.mzx.gulimall.cart.service.GuliWebCartService;
import com.mzx.gulimall.cart.vo.CartParamVo;
import com.mzx.gulimall.cart.vo.TotalCountTo;
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
     * <p>
     * 使用三元运算符之前的代码.
     * if (isLogin) {
     * <p>
     * // 用户登录状态的购物车列表.
     * // 该返回值可能返回空值.
     * return this.loginTrue(request, model);
     * <p>
     * } else {
     * <p>
     * // 用户未登录状态下的购物车.
     * return this.loginFalse(request, model);
     * <p>
     * }
     *
     * @param request
     * @param model
     * @param isLogin 表示当前是否登录: true表示登录状态.
     */
    @Override
    public Cart cart(HttpServletRequest request, Model model, boolean isLogin) {

        // 改用三元运算符.
        return isLogin ? this.loginTrue(request, model) : this.loginFalse(request, model);

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

    @Override
    public Map<String, Object> delete(Long skuId) {

        UserInfoTo userInfoTo = CartInterceptor.local.get();
        return (userInfoTo.getUserId() == null || userInfoTo.getUserId() <= 0) ?
                this.deleteKey(skuId, userInfoTo.getUserKey()) :
                this.deleteKey(skuId, userInfoTo.getUserId().toString());

    }

    private Map<String, Object> deleteKey(Long skuId, String key) {

        BoundHashOperations<String, Object, Object> boundHash = this.getBoundHash(key);
        Long delete = boundHash.delete(String.valueOf(skuId));
        HashMap<String, Object> map = new HashMap<>();
        if (delete == 1) {

            map.put("code", 200);

        } else {

            map.put("code", 500);

        }

        return map;

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
     * <p>
     * 需要做的是将当前用户未登录状态的离线购物车合并到用户真实的购物车.
     *
     * @param request
     * @param model
     */
    private Cart loginTrue(HttpServletRequest request, Model model) {

        UserInfoTo userInfoTo = CartInterceptor.local.get();
        CompletableFuture<Void> mergeFuture = CompletableFuture.runAsync(() -> {

            // 将离线购物车里面的内容添加到在线购物车. 该方法没有返回值.
            this.mergeCart(userInfoTo);

        }, executor);

        CompletableFuture<Void> deleteFuture = CompletableFuture.runAsync(() -> {

            // 合并之后应该进行移除离线购物车中当前用户存储的购物信息.
            String userKey = CurrentStringUtils.append(new StringBuilder(), StringConstant.CART_PREFIX,
                    userInfoTo.getUserKey());
            // 删除临时购物车根据默认生成的user-key
            stringRedisTemplate.delete(userKey);

        }, executor);

        Cart cart = new Cart();
        CompletableFuture<Void> packageFuture = CompletableFuture.runAsync(() -> {

            // 删除之后,从缓存中查询出当前用户购物车内的信息,并且返回给上一层.
            // 这里重新从Redis中查询出来的时候合并之后的购物车.
            List<CartItem> list = this.getAllCartItem(userInfoTo.getUserId().toString());
            cart.setItems(list);
            TotalCountTo totalCountTo = this.getTypeCountFromListCartItem(list);
            // 设置类别的数量.
            cart.setCountType(totalCountTo.getTypeCount());
            // 设置总共的数量.
            cart.setCount(totalCountTo.getCount());
            if (totalCountTo.getTotalPrice() != null) {

                cart.setTotalPrice(totalCountTo.getTotalPrice());

            }

            log.info("合并成功了: {}", cart.toString());

        }, executor);

        // 现在需要做的就是保证上面三个任务都执行完毕之后,在对controller进行返回.
        try {

            // 等待上面三个任务执行完毕. 不需要返回值.
            CompletableFuture.allOf(mergeFuture, deleteFuture, packageFuture).get();

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();
        }

        return cart;

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

        System.out.println(bigDecimal + "------");
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

    @Override
    public BoundHashOperations<String, Object, Object> getBoundHash(String key) {

        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(
                CurrentStringUtils.append(new StringBuilder(), StringConstant.CART_PREFIX, key));
        return ops;

    }

    private void addCartToRedis(CartItem item, Long skuId) {

        // 增加之前应该判断是离线状态还是用户已经登录状态.
        UserInfoTo userInfoTo = CartInterceptor.local.get();
        if (StringUtils.isEmpty(userInfoTo.getUserId())) {

            // 说明现在用户是未登录状态
            // 用户未登录状态应该设置过期时间.
            // 那么传进去的key就是用户的user-key.
            this.addCartToRedis(item, userInfoTo.getUserKey(), skuId, true);

        } else {

            // 现在用户是登录状态.
            // 那么传进去的key是用户的ID.
            this.addCartToRedis(item, userInfoTo.getUserId().toString(), skuId, false);

        }

    }


    /**
     * 第二个参数key和cart: 一起组成一个hash来表示键值。
     *
     * @param item
     * @param key
     * @param skuId
     */
    private void addCartToRedis(CartItem item, String key, Long skuId, boolean expreise) {

        BoundHashOperations<String, Object, Object> ops = this.getBoundHash(key);
        String jsonString = JSON.toJSONString(item);
        // 这里直接使用ops进行put操作.
        // TODO: 向Redis中增加商品失败.
        // 这里为什么会失败啊?
        // 现在情况是如果想Redis中添加Hash类型数据,那么就会报错.
        HashMap<String, String> map = new HashMap<>();
        // hashMap里面存放的是JSON.
        map.put(skuId.toString(), jsonString);
        // 使用 ops对其进行操作.
        ops.putAll(map);
        // 购物车需要设置有效时间吗?  设置一个月的吧
        // 设置过期时间为一个月.
        // 这里的键是那个hash键.
        // TODO:  这个键一定是正确的吗？
        // 一定要保证这个key一定是正确的.
        // TODO: 如果当前用户是登录状态那么就不应该设置过期时间.
        if (expreise) {

            stringRedisTemplate.expire(CurrentStringUtils.append(new StringBuilder(), StringConstant.CART_PREFIX, key),
                    DateTimeUtils.getOneMonthTimeSeconds(), TimeUnit.SECONDS);

        }

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

    /**
     * 将在线和不在线的购物车进行合并.
     *
     * @param userInfoTo 当前用户的信息. 当前用户可能没有登录,可能登录.
     * @return
     */
    private void mergeCart(UserInfoTo userInfoTo) {

        // 先获取用户当前的购物车.
        List<CartItem> onLines = this.getAllCartItem(userInfoTo.getUserId().toString());
        // 还需要将当前用户未登录状态购物车的内容合并到用户的购物车.
        List<CartItem> offLines = this.getAllCartItem(userInfoTo.getUserKey());

        // 现在需要获取到,
        BoundHashOperations<String, Object, Object> boundUserKeyHash = this.getBoundHash(userInfoTo.getUserKey());

        BoundHashOperations<String, Object, Object> boundUserIdHash = this.getBoundHash(
                userInfoTo.getUserId().toString());
        if (offLines != null && offLines.size() > 0) {

            // 表示当前用户已经添加了购物项的购物车.
            List<Long> skus = this.extractSkuIdFromCart(onLines);
            // 对离线购物车进行遍历, 将之前存在的仅仅是添加与之对应的数量,否则这个都添加进去.
            for (CartItem item : offLines) {

                if (skus.contains(item.getSkuId())) {

                    // 这里仅仅修改数量.
                    // 1. 先获取离线购物车中的数量. 注意这里是JSON类型数据.
                    // 这里转换类型又TM出现错误了.
                    Object o = boundUserKeyHash.get(item.getSkuId().toString());
                    // TODO: 不知道这里有问题没?
                    CartItem cartItem = JSON.parseObject(o.toString(), CartItem.class);
                    // 表示离线购物车当前商品添加的数量.
                    Integer count = cartItem.getCount();
                    // 2. 修改用户线上购物车. 直接覆盖即可.
                    // 就算是需要直接覆盖,那么也需要先获取到原先的item.
                    Object o1 = boundUserIdHash.get(item.getSkuId().toString());
                    CartItem nowItem = JSON.parseObject(o1.toString(), CartItem.class);
                    nowItem.setCount(nowItem.getCount() + count);
                    // 覆盖.
                    HashMap<String, String> map = new HashMap<>();
                    String nowItemJson = JSON.toJSONString(nowItem);
                    map.put(item.getSkuId().toString(), nowItemJson);
                    // 应该将对应的CartItem数据再次存入Redis中.
                    // TODO: 这里的idHashPut没有起到作用.
                    System.out.println("修改Redis数据成功了");
                    boundUserIdHash.putAll(map);

                } else {

                    // 当前分支没有问题的.
                    // 如果自爱信啊购物车里不包含离线购物车内容, 那么就将当前item添加到在线购物车中.
                    HashMap<String, String> map = new HashMap<>();
                    map.put(item.getSkuId().toString(), JSON.toJSONString(item));
                    System.out.println("增加Redis数据成功了.");
                    boundUserIdHash.putAll(map);

                }

            }

        }

    }

    private List<Long> extractSkuIdFromCart(List<CartItem> list) {

        /*
         * --------------------------------------------------------
         * 将所有的skuID抽取成一个集合.
         * --------------------------------------------------------
         * */
        List collect = null;
        if (list != null) {

            collect = list.stream().map(item -> {

                return item.getSkuId();

            }).collect(Collectors.toList());

        }

        return collect;

    }


    private TotalCountTo getTypeCountFromListCartItem(List<CartItem> items) {

        if (items != null && items.size() > 0) {

            TotalCountTo totalCountTo = new TotalCountTo();

            items.stream().forEach(item -> {

                // 遍历每一个元素并且找到总得类别和总的数量.
                totalCountTo.setCount(totalCountTo.getCount() + item.getCount());
                totalCountTo.setTotalPrice(totalCountTo.getTotalPrice().add(item.getTotalPrice()));

            });

            totalCountTo.setTypeCount(items.size());
            return totalCountTo;

        }

        return new TotalCountTo();

    }


}
