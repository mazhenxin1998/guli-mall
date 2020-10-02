package com.mzx.gulimall.product.utils;

import com.mzx.gulimall.annotation.constant.StringIpConstant;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-30 21:21 周三.
 */
public class ProductIpUtil {

    public static String getIp(HttpServletRequest request) {

        String ip = request.getHeader(StringIpConstant.X_FORWARDED_FOR);
        if (ip == null || ip.length() == 0 || StringIpConstant.UNKNOWN.equalsIgnoreCase(ip)) {

            ip = request.getHeader(StringIpConstant.PROXY_CLIENT_IP);

        }

        if (ip == null || ip.length() == 0 || StringIpConstant.UNKNOWN.equalsIgnoreCase(ip)) {

            ip = request.getHeader(StringIpConstant.WL_PROXY_CLIENT_IP);

        }

        if (ip == null || ip.length() == 0 || StringIpConstant.UNKNOWN.equalsIgnoreCase(ip)) {

            ip = request.getHeader(StringIpConstant.HTTP_CLIENT_IP);

        }

        if (ip == null || ip.length() == 0 || StringIpConstant.UNKNOWN.equalsIgnoreCase(ip)) {

            ip = request.getHeader(StringIpConstant.HTTP_X_FORWARDED_FOR);

        }

        if (ip == null || ip.length() == 0 || StringIpConstant.UNKNOWN.equalsIgnoreCase(ip)) {

            ip = request.getRemoteAddr();

        }

        // 现在将ip转换为数字类型.
        return ip;

    }

}
