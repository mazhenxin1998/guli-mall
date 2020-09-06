package com.mzx.gulimall.product.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ZhenXinMa
 * @date 2020/9/5 17:49
 */
public interface LoginService {

    /**
     * 为每个用户分配一个默认的键.
     *
     * @param response
     */
    void addUserKey(HttpServletRequest request,HttpServletResponse response);
}
