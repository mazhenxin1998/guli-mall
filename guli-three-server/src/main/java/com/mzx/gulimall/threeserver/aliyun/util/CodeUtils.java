package com.mzx.gulimall.threeserver.aliyun.util;

import java.util.ArrayList;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 17:38
 */
public class CodeUtils {

    public static char[] c =
            {'a','b','c','d','e','f','g','h',
            'i','g','k','l','m','n','o','p',
            'q','r','s','t','u','v','w','y',
            'z','A','B','C','D','E','F','G',
            'H','I','J','K','L','M','N','O',
            'P','Q','R','S','T','U','V','W',
            'X','Y','Z'};

    public static String getCode(){

        StringBuilder builder = new StringBuilder();
        // 0 - 1
        int random = (int) (Math.random() * 10);
        // 转换成int类型
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(3);
        integers.add(5);
        integers.add(7);

        for (int i = 0; i < 6; i++) {

            if (integers.contains(random)) {

                // 说明现在是奇数.
                // 奇数则使用字母.
                // 这里永远是0
                int r = (int) (Math.random() * 40);
                builder.append(c[r]);
            } else{

                // 否则就用0到1的数字.
                int r2 = (int) (Math.random() * 10);
                builder.append(r2);
            }

            random = (int) (Math.random() * 10);
        }

        return builder.toString();

    }

}
