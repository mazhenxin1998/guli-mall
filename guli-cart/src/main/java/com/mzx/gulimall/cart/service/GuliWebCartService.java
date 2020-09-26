package com.mzx.gulimall.cart.service;

import com.mzx.gulimall.cart.vo.CartParamVo;
import com.mzx.gulimall.common.model.Cart;
import com.mzx.gulimall.common.model.CartItem;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2020/9/5 17:29
 */
public interface GuliWebCartService {

    /**
     * 对购物车上的数据进行封装操作.
     * <p>
     * 如果用户没有进行登录,那就查询当前用户的临时购物车,如果用户当前登录了,那么就将临时购物车和当前的购物车进行合并.
     *
     * @param request
     * @param model
     * @param isLogin 表示当前是否登录: true表示登录状态.
     * @return
     */
    Cart cart(HttpServletRequest request, Model model, boolean isLogin);

    /**
     * 向购物车增加商品.
     *
     * @param request
     * @param param
     * @param model
     * @return
     */
    CartItem add(HttpServletRequest request, CartParamVo param, Model model);

    /**
     * 根据参数中给定的SkuId进行删除.
     *
     * @param skuId
     * @return
     */
    Map<String, Object> delete(Long skuId);

    /**
     * 根据指定的Key获取指定的Map类型的数据进行操作.
     *
     * @param key
     * @return
     */
    BoundHashOperations<String, Object, Object> getBoundHash(String key);
}
