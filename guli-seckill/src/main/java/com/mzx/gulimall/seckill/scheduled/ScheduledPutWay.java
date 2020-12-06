package com.mzx.gulimall.seckill.scheduled;

import com.mzx.gulimall.seckill.constant.SeckillConstant;
import com.mzx.gulimall.seckill.service.ISecKillService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 为了完成非阻塞式的定时任务，需要结合异步任务来实现.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 18:23 周二.
 */
@Component
public class ScheduledPutWay {

    @Autowired
    private ISecKillService iSecKillService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 秒 分 时 日 周 月.
     * '*'表示任意.
     * <p>
     * 定时上架秒杀商品。
     * <p>
     * 先上架秒杀,上架秒杀接口幂等性之后来实现.
     */
    @Async
    @Scheduled(cron = "0/5 * * * * *")
    public void putWay() {

        /*
         * 到时候秒杀任务肯定是多态服务进行部署的，既然是多态部署，那么就只能允许一个任务先进行，其余的后进行.
         *
         *  使用分布式锁来进行进程之间控制.
         *
         * */
        RLock lock = redissonClient.getLock(SeckillConstant.SECKILL_PUBLISH_IDEMPOTENT);
        // 默认10S,如果没有执行完成，则自动释放锁.
        lock.lock(15L, TimeUnit.SECONDS);
        try {

            System.out.println("定时任务执行了...");
            iSecKillService.putWay();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            lock.unlock();
        }

    }

}
