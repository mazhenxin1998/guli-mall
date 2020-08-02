package com.mzx.gulimall.product.dao;

/**
 * @author ZhenXinMa
 * @date 2020/7/26 16:10
 */
public class MainTest {

    /**
     * Integer 默认是空, 八个基本类型没有复制的情况下为该类型的默认值.
     * 对象没有初始化之前为null.
     */
    private static Integer i;

    public static void demo2() {
        if (i == 0) {

            System.out.println("A");
        } else {

            System.out.println("B");
        }
    }


    public static void main(String[] args) {

        /**
         * Integer在-127 - 127 之内 类似String字符串 有个池去维护.
         */
        Integer t1 = 100;
        Integer t2 = 100;
        Integer t3 = 200;
        Integer t4 = 200;
        String s1 = "s";
        String s2 = "s";
        System.out.println(s1 == s2);
        System.out.println(t1 == t2);
        System.out.println(t3 == t4);
        MainTest.demo2();
        ;


    }


}
