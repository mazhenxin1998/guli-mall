package com.mzx.gulimall.util;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ZhenXinMa
 * @date 2020/9/1 10:16
 */
public class MutexTest {

    public static volatile int status = 1;

    public static void main(String[] args) {

        Mutex lock = new Mutex();
        for (int i = 0; i < 30; i++) {

            // 对于线程来说,要实现线程之间的同步,必须
            new Thread(() -> {

                // 为什么使用可重入锁就不行？
                // 可重入锁不行吗?
                lock.lock();
                try {

                    System.out.println("我是线程: " + Thread.currentThread() + "输出: " + status);
                    status++;
                } catch (Exception e) {

                    e.printStackTrace();
                } finally {

                    lock.unlock();
                }


            }, "" + i).start();
        }


    }


}
