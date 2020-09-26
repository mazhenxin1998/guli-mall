package com.mzx.gulimall.cart.service.impl;

import com.mzx.gulimall.cart.constant.StringConstant;
import com.mzx.gulimall.cart.interceptor.CartInterceptor;
import com.mzx.gulimall.cart.service.IGuliCartService;
import com.mzx.gulimall.cart.vo.UserInfoTo;
import com.mzx.gulimall.common.model.CartItem;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.util.CurrentStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-26 17:04 周六.
 */
@Service
public class GuliCartServiceImpl implements IGuliCartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R getCheckedCartItems() {

        // TODO: 2020/9/26 feign远程调用将会丢失请求头,也就是说这里获取到得userInfoTo将会使一个null.
        UserInfoTo userInfoTo = CartInterceptor.local.get();
        BoundHashOperations<String, Object, Object> ops = userInfoTo.getUserId() == null || userInfoTo.getUserId() <= 0 ?
                this.getBoundHash(userInfoTo.getUserKey()) : this.getBoundHash(userInfoTo.getUserId().toString());
        // items当前状态下
        List<Object> items = ops.values();
        // 强制转换.
        List<Object> list = items.stream().filter(item -> {

            CartItem cartItem = (CartItem) item;
            // 返回true表示保留.
            return cartItem.isCheck() ? true : false;

        }).collect(Collectors.toList());
        return R.ok().put("data",list);

    }


    private BoundHashOperations<String, Object, Object> getBoundHash(String key) {

        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(
                CurrentStringUtils.append(new StringBuilder(), StringConstant.CART_PREFIX, key));
        return ops;

    }

}
