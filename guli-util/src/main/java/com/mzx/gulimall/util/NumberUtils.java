package com.mzx.gulimall.util;

import com.sun.istack.internal.NotNull;

/**
 * @author ZhenXinMa
 * @date 2020/9/8 19:19
 */
public class NumberUtils {

    public static final int MIN_VALUE = 0x80000000;

    public static final int MAX_VALUE = 0x7fffffff;

    /**
     * 返回指定范围内大小的随机整数.
     * <p>
     * 需要注意的是: 如果参数给的范围小于等于0那么就返回0,并且如果参数给的范围大于Integer的最大值,那么也返回0.
     *
     * @param size 指定范围.
     * @return
     */
    public static Integer getAppointRandomNumber(@NotNull int size) {

        if (size <= 0 || size >= MAX_VALUE) {

            return 0;

        }

        return (int) Math.random() * size;

    }


}
