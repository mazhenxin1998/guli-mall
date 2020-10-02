package com.mzx.gulimall.order.constant;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 00:09 周五.
 */
public class RedisConstant {

    public static final String LIMIT_PREFIX_KEY = "LIMIT:";

    /**
     * 保存每一个订单页面的token.
     * 生产环境中应该保存在独立的Redis服务中.
     */
    public static final String TOKEN_UUID = "GULI:TOKEN:";

}
