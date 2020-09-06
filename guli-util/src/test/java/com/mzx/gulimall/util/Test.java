package com.mzx.gulimall.util;

import sun.misc.Unsafe;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ZhenXinMa
 * @date 2020/9/1 10:27
 */
public class Test implements Runnable {

    private int i = 0;
    ReentrantLock lock = new ReentrantLock();


    @Override
    public void run() {

        // 每个线程运行的时候,都将i进行+1，在锁内.
        // 这个锁不是一个锁.
        lock.lock();
        try {
            System.out.println("我是线程: " + Thread.currentThread() + "输出: " + i);
            // 这个i被修改之后, 能被其他感知吗？
            i++;
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            lock.unlock();
        }

    }
}
