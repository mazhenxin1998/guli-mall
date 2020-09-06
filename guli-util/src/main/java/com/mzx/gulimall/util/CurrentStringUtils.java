package com.mzx.gulimall.util;

/**
 * @author ZhenXinMa
 * @date 2020/8/31 20:50
 */
public class CurrentStringUtils {

    /**
     * 使用StringBuilder进行字符串的拼接来节省时间和内存.
     * <p>
     * 返回的结果和传入的第一个参数有密切的关系,如果说多次调用该方法,传入的StringBuilder将会这几次多方法调用的String字符串进行累加.
     *
     * @param builder 传入的StringBuilder累加器.
     * @param args    要拼接的字符串数组.
     * @return 在最短的时间内以及占用最低内存为代价返回参数args拼接后的结果.该返回值要么返回null, 要么返回正确的值, 返回null是因为出现异常了.
     */
    public static String append(StringBuilder builder, String... args) {

        try {

            if (builder != null) {

                for (String arg : args) {

                    builder.append(arg);
                }

                return builder.toString();
            }

            return null;
        } catch (Exception e) {

            System.out.println(e);
            return null;
        }
    }

    public void noBuilder() {

        String src = "";
        long s = System.currentTimeMillis();
        for (int i = 0; i < 500000; i++) {

            // 大约耗费3秒左右.
            src = src + "a";
        }

        long e = System.currentTimeMillis();
        System.out.println(e - s);
    }

    public void useBuilder(){

        long s = System.currentTimeMillis();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 500000; i++) {

            // 大约花费时间5毫秒左右
            builder.append("a");
        }

        long e = System.currentTimeMillis();
        System.out.println(e - s);
    }

}
