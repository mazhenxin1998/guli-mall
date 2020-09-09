package com.mzx.gulimall;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {


    public static void main(String[] args) {

        // 产生指定范围内随机数.
        int size = 8;
        for (int i = 0; i < 30; i++) {
            int random = (int) (Math.random() * size);
            // double转换成int类型.

            System.out.println(random);
        }



    }


}
