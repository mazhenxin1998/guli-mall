package com.mzx.gulimall.cart.api;

import com.mzx.gulimall.cart.vo.CartParamVo;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

/**
 * @author ZhenXinMa
 * @date 2020/9/4 7:21
 */
public interface GuliWebCartControllerApi {

    /**
     * 统一提供用户前往购物车列表.
     *
     * @param request 当前请求.
     * @param model   域数据.
     * @return
     */
    String cart(HttpServletRequest request, Model model);

    /**
     * 接受用户请求将一个SKU添加到购物车.
     * <p>
     * 这个时候带上request是因为添加购物车也是有可能是用户在没有登录状态下进行保存的.
     * 带上Model是因为在添加成功之后是需要跳转到添加购物成功的页面的.
     *
     * @param request 当前请求.
     * @param param   当前添加的商品.
     * @param model   域数据.
     * @return
     */
    String addCart(HttpServletRequest request,
                   CartParamVo param, BindingResult bindingResult,
                   Model model) throws ExecutionException, InterruptedException;

    /**
     * 重定向解决表单重复提交.
     * <p>
     * 参数是SKU的ID.
     *
     * @param id
     * @return
     */
    String toCartSuccess(Long id, Model model);

    /**
     * 根据SkuId进行删除.
     *
     * @param skuId
     * @return
     */
    Object delete(Long skuId);

}
