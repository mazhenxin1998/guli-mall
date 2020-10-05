package com.mzx.gulimall.main;

import java.math.BigDecimal;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-05 18:14 周一.
 */
public class MainTest {


    public static void main(String[] args) {

        BigDecimal total = new BigDecimal(20);
        BigDecimal b = new BigDecimal(60);
        total = total.add(b);
        System.out.println(total.intValue());


    }


}
