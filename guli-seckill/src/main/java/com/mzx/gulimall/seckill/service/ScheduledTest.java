package com.mzx.gulimall.seckill.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 为了解决定时任务阻塞的缺点,所以使用Spring异步任务.
 * 1. @EnableAsync 表示开启异步任务.
 * 2. @Async 表示当前方法是一个异步任务.
 *
 * @author ZhenXinMa.
 * @EnableScheduling
 * @EnableAsync
 * @slogan 脚踏实地向前看.
 * @create 2020-11-30 23:51 周一.
 */

@Slf4j
public class ScheduledTest {

    int i = 0;

    /**
     * 克隆表达式语法: 秒 分 时 日 月 周 年(spring不支持)
     * 特殊字符含义:
     * 1. ',' 枚举. 1,3,4 表示任意时刻的第一秒第三秒第四秒都启动该任务.
     * 2. '-' 范围. 7-20 表示7到20秒之间，每秒启动一次,如果是在分钟上,那么就是每分钟执行一次.
     * 3. '/' 步长. 1/5 表示从第一秒开始执行,每隔5秒之后执行一次.
     * 4. '*' 任意.
     * 5. '?' 解决冲突问题. 在日和周几上，只有有一个是确定了或者是*，那么另一个必须使用？指定.
     * 6. '#' 第几个. cron = '* * * ? * 5#2' 每个月的第2个周4.
     * 7. 'L' 出现在日和周上面. Last最后一个. cron(* * * ? * 3L) 表示每个月的最后一个周二.
     * 8. 'W' 工作日. cron(* * * W * ?) 表示每个月的工作日都触发.
     * <p>
     * 定时任务表示在Corn表示触发器内该方法就会执行,执行的是整个方法.
     * 异步任务表示每次执行该方法的时候都开启一个线程,所以定时任务和异步任务结合起来可以完成定时任务异步效果.
     */
    @Async
    @Scheduled(cron = "1/5 * * * * *")
    public void s1() throws InterruptedException {

        /*
         *
         * 定时任务中不允许阻塞,并且Spring定时任务框架中是阻塞执行的.
         * 定时任务不应该阻塞，默认是阻塞的.
         *      1. 可以让业务运行以异步的方式，自己提交到线程池:
         *      2. SpringBoot支持定时任务线程池.
         *         配置文件: TaskSchedulingProperties
         *         绑定的配置前缀属性: spring.task.scheduling
         *         该方式不好使.
         *      3. 异步任务.
         *         异步任务的自动配置: TaskExecutionAutoConfiguration
         *
         * */
        log.info("执行一次");
        Thread.sleep(10000);

    }

}
