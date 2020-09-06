package com.mzx.gulimall.product.dao;

import org.junit.Test;

import java.util.ArrayList;

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

    @Test
    public void t3(){

        // 自动装箱.
        int a = 1000;
        Integer b = new Integer(1000);


        Integer integer = new Integer(200);
        Integer integer1 = new Integer(200);
        // false。// Integer内部维护了一个池.

        Integer i1 = new Integer(50);
        Integer i2 = new Integer(50);

        int i3 = 150;
        int i4 = 150;
        System.out.println(i3 == i4);

    }

    public static void demo2() {
        if (i == 0) {

            System.out.println("A");
        } else {

            System.out.println("B");
        }
    }

    @Test
    public void t() {

        //  14毫秒是仅仅只有字符串来进行的.
        // 108毫秒是有字符串引用来进行字符串拼接的.
        ArrayList<String> integers = new ArrayList<>();
        // 不使用StringBuilder。
        long start = System.currentTimeMillis();
        // 带有引用的不能使用+号进行拼接.
        for (int j = 0; j < 1000000; j++) {

            String s1 = new String("" + j);
            String s = s1 + "b";
            // 保持s对象的引用不会被弄到GCROOTS.
            integers.add(s);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start));

    }

    @Test
    public void t2() {

        ArrayList<String> integers = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        // 不使用StringBuilder。
        long start = System.currentTimeMillis();
        // 带有引用的不能使用+号进行拼接.
        for (int j = 0; j < 1000000; j++) {

            builder.append(j);
            // 保持s对象的引用不会被弄到GCROOTS.
            // StringBuilder类的toString类不会再常量池创建对象,而是在堆中创建对象.
            integers.add(builder.toString());
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start));
    }

    /**
     * 测试+.
     */
    @Test
    public void test() throws InterruptedException {

        // 3432毫秒.
        long start = System.currentTimeMillis();
        String src = "";
        for (int j = 0; j < 1000000; j++) {

            // 如果有引用作拼接运算,那么将会在堆空间中创建出一个String类的对象.
            // 每次循环都会创建一个StringBuilder出来, 那么应该会调用出来1000000个StringBuilder实例.
            src = src + "a";
        }

        long end = System.currentTimeMillis();
        System.out.println((end - start));
        Thread.sleep(1000000);

    }

    @Test
    public void testBuilder() throws InterruptedException {

        // 7毫秒.
        long start = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < 100000; j++) {

            // 如果这样做会更占用内存空间. ?
            // append返回的是当前实例.
//            builder.append(j);
            // 使用StringBuilder创建的实例多.
            builder.append("a");
        }

        builder.toString();
        long end = System.currentTimeMillis();
        System.out.println((end - start));
        Thread.sleep(100000);
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
