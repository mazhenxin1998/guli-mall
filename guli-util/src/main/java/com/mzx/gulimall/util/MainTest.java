package com.mzx.gulimall.util;

import java.io.Console;

/**
 * @author ZhenXinMa
 * @date 2020/9/2 12:40
 */
public class MainTest {


    public static int getHigh(int... args) {

        // 找出最大元素.
        // 第一趟.
        int temp = 0;
        for (int i = 0; i < args.length; i++) {

            int high = 0;
            for (int j = i + 1; j < args.length; j++) {

                // 进入该分支, 表示当前值要大于后面要比较的值.
                if (args[i] > args[j]) {

                    // 找出第一趟中最大的值.
                    if (args[i] > temp) {

                        temp = args[i];
                    }
                }
            }
        }

        return temp;
    }

    /**
     * 接雨水.
     *
     * @param args
     */
    public static void rain(int... args) {

        int num = 0;
        // 先获取到该数组中雨水最高的哪一个,方便为了进行遍历》
        int high = getHigh(args);
        // 0表示高度为0 既然高度为0 那么就不可能接受到雨水.
        // 第一层循环表示的是层数.
        // 表示高度.
        for (int i = 1; i <= high; i++) {

            // 遍历高度为1
            // 现在要排除第一个和最后一个不能接受雨水,只有中间的才可以接受雨水 .
            // 第二层循环表示从该层中中挨个比较.
            // 从第一层开始比较.
            // 表示长度.
            for (int j = 0; j < args.length; j++) {

                if (args[j] == 0) {

                    // 如果当前列的高度为0,那么可以直接跳过,不用丛比较.
                    // continue跳出当前循环,执行下次循环.

                } else {

                    if (j == args.length - 1) {

                        // 最后一个不用比较.
                    } else {


                        // 代码执行到了这次,说j列不是空,那么就从这列开始.
                        // 只要j后面的有一个不为空就可以接受雨水.
                        // 还需要有个保证就是 j+1 一定是空的,这样才可以接受雨水.
                        if (args[j + 1] != 0) {

                        } else {

                            // 执行到了这里,表示有接水的前提.
                            for (int k = j + 1; k < args.length; k++) {

                                // 接受雨水前一定要保证k后面一定要有一个不为空.
                                // k表示当前j列的后一个.
                                // 如果代码能执行到这里,表示第一个k一定为空.
                                if (args[k] != 0) {

                                    continue;
                                } else {

                                    // 表示可以接雨水.
                                    // 而且每一个args[k] 都不能比args[j] 低
                                    if (args[k + 1] >= args[j]) {

                                        // 表示可以接受雨水.
                                        num++;

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        System.out.println(num);
    }


    public static void main(String[] args) {

//        rain(new int[]{0, 2, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1});

        synchronized (MainTest.class){
            System.out.println(11);
        }


    }


}
