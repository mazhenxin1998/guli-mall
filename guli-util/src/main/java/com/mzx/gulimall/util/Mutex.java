package com.mzx.gulimall.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author ZhenXinMa
 * @date 2020/9/1 9:50
 */
public class Mutex implements Lock {

    /**
     * 仅仅只是需要将对锁的状态改变的上层接口交给Mutex来管理即可.
     */
    private final Sync sync = new Sync();

    @Override
    public void lock() {

        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {

        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }

    @Override
    public void unlock() {

        // 如果要释放锁,那么就说明，当前锁的该实例由当前线程获取.
        sync.release(1);
    }

    /**
     * 不知道Condition在锁中的作用是什么?
     * @return
     */
    @Override
    public Condition newCondition() {

        return sync.condition();
    }

    /**
     * 判断当前锁是否被锁.
     * @return
     */
    public boolean isLock(){

        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads(){

        return sync.hasQueuedThreads();
    }

    /**
     * 通过内部类实现锁,在通过实现了Lock接口的类对其进行显示调用.
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 返回当前锁的状态: 通过调用getState()方法,如果返回1表示当前锁被占用. 如果为0表示未被占用.
         * <p>
         * 当前锁是否被占用.
         *
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {

            return this.getState() == 1;
        }

        /**
         * 通过CAS尝试获取锁(该CAS是QAS已经对其包装过的CAS).
         * <p>
         * 如果要看JDK源码的CAS那么可以看QAS中提供该CAS的方法可以知道其是调用了unsafe方法.
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {

            // 尝试获取锁那么就说明参数arg默认是0.

            // 通过CAS来获取锁的状态,
            // CAS自旋: 又期望的值0到1.
            // CAS使用过程中不会导致线程中断.
            // 只要没有修改到过期的数据,那么就一直循环进行修改.
            // 高并发环境下, 该判断也不回出现并发安全问题。
            if (compareAndSetState(0, 1)) {

                // 表示获取到锁.
                this.setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }

            return false;
        }

        /**
         * 释放锁.
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {

            // 如果当前没有上锁，就去释放锁，那么就抛出异常.
            if (this.getState() == 0) {

                throw new IllegalMonitorStateException();
            }

            // 只要是能执行到这里的,
            // 将当前锁被哪个线程所拥有设置为空.
            this.setExclusiveOwnerThread(null);
            this.setState(0);
            return true;
        }

        // 返回一个Condition，每个Condition都包含了一个condition队列.
        Condition condition() {

            return new ConditionObject();
        };

    }


}
