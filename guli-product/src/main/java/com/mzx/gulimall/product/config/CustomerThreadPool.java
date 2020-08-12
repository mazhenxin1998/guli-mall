package com.mzx.gulimall.product.config;

import java.util.concurrent.*;

/**
 * @author ZhenXinMa
 * @date 2020/8/12 16:32
 */
public class CustomerThreadPool extends ThreadPoolExecutor {

    public CustomerThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }



}
