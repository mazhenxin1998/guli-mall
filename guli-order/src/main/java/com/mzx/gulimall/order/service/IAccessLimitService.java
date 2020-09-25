package com.mzx.gulimall.order.service;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 00:13 周五.
 */
public interface IAccessLimitService {

    /**
     * 向Redis中设置key并且设置过期时间.
     *
     * @param key
     * @param maxCount
     * @param seconds
     */
    void setKeyWithExpiration(String key, int maxCount, int seconds);

    /**
     * 根据指定的key将Value查询出来,该Value可能是不存在的.
     *
     * @param key
     * @return 返回key所对应的value.
     */
    String getValue(String key);

    /**
     * Redis自增操作.
     * @param key
     */
    void keyIncrement(String key);
}
