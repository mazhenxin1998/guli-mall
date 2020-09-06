package com.mzx.gulimall.util;

/**
 * 提供一些常用的静态时间接口.
 * <p>
 * 比如说提供一个静态获取一个月时间的所对应的秒数.
 *
 * @author ZhenXinMa
 * @date 2020/9/6 0:28
 */
public class DateTimeUtils {

    /**
     * 获取一个月的时间所对应的秒数.
     * @return
     */
    public static final Integer getOneMonthTimeSeconds(){

        // 显示装箱.
        return Integer.valueOf(30 * 24 * 60 * 60);
    }


}
