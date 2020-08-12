package com.mzx.gulimall.product.web;

import com.mzx.gulimall.product.entity.CategoryEntity;
import com.mzx.gulimall.product.service.CategoryService;
import com.mzx.gulimall.product.vo.web.Catalog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 类路径是resources:/  这个是类路径 静态资源放在类路径下面的resource下面才可以.
 *
 * @author ZhenXinMa
 * @date 2020/8/2 13:02
 */
@Slf4j
@Controller
public class WebController {

    private static final Object lock = new Object();

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CachingTest cachingTest;


    @GetMapping(value = {"/", "index.html", "index"})
    public String index(Model model) {

        /*
         * --------------------------------------------------------
         * 需要将所有的一级分类放到request域中.
         * --------------------------------------------------------
         * */
        List<CategoryEntity> categoryEntityList = categoryService.findOneCategoryList();
        log.info(categoryEntityList.toString());
        model.addAttribute("categorys", categoryEntityList);
        return "index";
    }

    @GetMapping(value = "/web/index/json/catalog.json")
    @ResponseBody
    public Map<String, List<Catalog2Vo>> getCatalog() {

        /*
         * --------------------------------------------------------
         * 返回值以一级分类的ID作为Key，其子分类作为其Key的值.
         * --------------------------------------------------------
         * */
        return categoryService.findAllCatagory();
    }

    @GetMapping(value = "/cache")
    @ResponseBody
    public String testSpringCache() {

        return cachingTest.t1();
    }


    @GetMapping(value = "/test/lock")
    @ResponseBody
    public String testRedissonLock() {

        RLock lock = redissonClient.getLock("synlock");
        lock.lock();
        String result = "";
        try {

            // 正常业务 休眠半分钟.
            Thread.sleep(50000);
            result = "正常业务加锁(分布式锁)成功";
        } catch (Exception e) {

            result = e.getMessage();
            System.out.println(e);
        } finally {

            lock.unlock();
        }

        return result;
    }

    @GetMapping(value = "/product/test/{id}")
    @ResponseBody
    public String test(@PathVariable Long id) {

        /*
         * --------------------------------------------------------
         * 模拟查询商品根据ID,有缓存从缓存中查,没有则从DB中查.
         * --------------------------------------------------------
         * */

        // 当由用户进来,先从Redis缓存缓存中查询
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        String test = ops.get("test:" + id);
        if (!StringUtils.isEmpty(test)) {

            System.out.println("从缓存中读取了数据....");
            // TODO: 这里有可能出现 "null" 字符串.
            if ("null".equals(test)) {

                // 到了这里说明，该次查询的是为了防止缓存穿透而设置的null缓存.
                System.out.println("成功解决了缓存穿透.");
                return test;
            }

            return test;
        } else {

            // TODO: 缓存击穿针对的是热点数据，在某一个时刻有大量的用户进行访问,在大量用户访问之前，key过期了，那么大量用户直接访问DB，会
            // TODO: 造成数据库宕机的可能性并且缓存击穿最有可能发生在电商项目上热点数据上.

            // 从数据库中查询
            String s = this.t1DBLock(id);
            // 如果从数据库中查出来的是null，那么为了防止在高并发环境下缓存穿透的问题,在这里也存入数据库.
            // TODO: 为了保证最终一致性,在这里对于正常的业务,设置缓存的key为7天,对于非正常的业务,设置为3天.

            // TODO: 解决了缓存穿透,现在又面临着缓存雪崩问题。在某一时刻,大面积的Key集体失效，而之后的某个时间点有100w+的用户进行并发访问,
            //  而我们的设计思路是先从缓存中取出数据,如果取不到再从DB中查询,但是100w+用户访问过期的数据,会导致DB的压力骤增至宕机,同时Redis
            //  的大量请求会积压,开始出现超时现象导致Redis服务宕机.
            // TODO: 解决办法是为每一个缓存加一个随机数的缓存时间.
            if ("null".equals(s)) {

                System.out.println("出现了缓存穿透的现象.");

            }

            // 正常.
            ops.set("test:" + id, s, 7, TimeUnit.DAYS);
            return s;
        }

    }

    @GetMapping(value = "/product/set/{count}")
    @ResponseBody
    public String setTotalCount(@PathVariable String count) {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        try {

            ops.set("SEM-LOCK", count);
            return "成功将车位修改成了: " + count + "个";
        } catch (Exception e) {

            System.out.println(e);
            return "抱歉: 修改出现异常.";
        }
    }

    @GetMapping(value = "/product/semaphore/{id}")
    @ResponseBody
    public String testSemaphore(@PathVariable String id) {

        String result = "";
        // 信号量锁: 当设置的信号量已经被所有用不占用完毕,那么新进来的用户就必须交进行等待，在这个期间如果释放了一个信号量，
        // 那么就有一个用户可以进行抢占一个信号量进行业务代码的执行.
        RSemaphore lock = redissonClient.getSemaphore("SEM-LOCK");
        try {

            // 设置信号量.
            // acquire是获取信号量. 带上参数就是获取几个信号量.
            // 如果当前没有可用的信号量,那么就阻塞的进行等待.直到了有了足够该次请求使用的信号量.
            lock.acquire();
            result = "车位: " + id + "已经被占用了.";
            System.out.println(result);
        } catch (Exception e) {

            System.out.println(e);
        }

        return result;
    }

    @GetMapping(value = "/product/go/{id}")
    @ResponseBody
    public String gogo(@PathVariable String id) {

        String result = "";
        RSemaphore lock = redissonClient.getSemaphore("SEM-LOCK");
        try {

            // 这里模拟车位I上的车主已经将车开走了.
            // 表示信号量 + 1;
            lock.release();
            result = "停车位: " + id + "已经空了,广播通知可以让空闲的车主进来停车了";
            System.out.println(result);
        } catch (Exception e) {

            System.out.println(e);
        }

        return result;
    }


    @GetMapping(value = "/product/countDownLatch")
    @ResponseBody
    public String testCountDownLatch() {

        String result = "";
        // 闭锁测试.
        // 为什么CountDownLatch要称之为闭锁呢?
        RCountDownLatch latch = redissonClient.getCountDownLatch("CD-LOCK");
        // 表示当前CountDownLatch中最大量是多少和信号量类似.
        latch.trySetCount(3);
        try {

            // 进行等待.
            // 如果Count值不会0则一直进行等待,如果为0则继续往下执行.
            // 阻塞式等待.
            latch.await();
            System.out.println("开始执行业务代码....");
            result = "好了，开始上课了";

        } catch (Exception e) {

            System.out.println(e);
        } finally {

            System.out.println("这里是不需要释放锁的...");
        }

        return result;
    }

    @GetMapping(value = "/product/countDownLatch/{id}")
    @ResponseBody
    public String testCountDownLatchDown(@PathVariable String id) {

        String result = "";
        RCountDownLatch latch = redissonClient.getCountDownLatch("CD-LOCK");
        try {

            // 每次当一个请求进来,总数减少1.
            // 当Count数减到0的时候,那么就会自动唤醒调用该锁await方法的线程,让其继续进行执行.
            latch.countDown();
            // 先减少在执行业务逻辑. 避免中途服务宕机出现死锁现象.
            result = "同学 : " + id + "已经坐到自己的座位上了.";
        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }


    @GetMapping(value = "/product/write")
    @ResponseBody
    public String testWriteLock() {

        // 获取写锁.
        // 读锁又称为排他锁.
        // 当前仅仅支持当前一把锁进行加锁,其他锁都不能进行抢占成功.
        String result = "";
        RReadWriteLock lock = redissonClient.getReadWriteLock("RW-LOCK");
        lock.writeLock().lock();
        try {

            System.out.println("写锁添加成功.");
            result = "写锁添加成功,正在执行业务... 业务执行时间大约是20S.";
        } catch (Exception e) {

        } finally {

            lock.writeLock().unlock();
            System.out.println("成功释放写锁.");
        }

        return result;
    }

    @GetMapping(value = "/product/read")
    @ResponseBody
    public String testReadLock() {

        String result = "";
        RReadWriteLock lock = redissonClient.getReadWriteLock("RW-LOCK");
        lock.readLock().lock();
        try {

            System.out.println("添加读锁成功了...我是用户: " + Thread.currentThread().getId());
            Thread.sleep(10000);
            result = "添加读锁成功...";
        } catch (Exception e) {
            System.out.println(e);
        } finally {

            lock.readLock().unlock();
            System.out.println("读锁释放了...");
        }

        return result;
    }


    /**
     * 分布式锁解决缓存击穿.
     *
     * @param id
     * @return
     */
    public String t1DBLock(Long id) {

        // TODO: 缓存击穿通过加锁来解决，就是为了避免过多的用户从DB中查询，因为我们知道他们查询的数据是一样的，
        //  所以我们只需要让一个用户查询返回我们将其存入缓存中供其他用户使用即可.
        // 既然要用锁,那么应该锁谁?
        String result = "";

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // 假设分布式锁现在没有过期时间.
        // 原子性的给该锁设置过期时间以防止出现死锁的情况.
        String uuid = UUID.randomUUID().toString();
        Boolean lock = ops.setIfAbsent("lock", uuid, 30, TimeUnit.SECONDS);

        if (lock) {

            // 锁没有设置自欧东过期的时间.
            System.out.println("线程:" + Thread.currentThread().getId() + "抢占分布式锁lock成功");
            try {

                result = this.getDataFromDB(id);
            } catch (Exception e) {
                System.out.println(e);
            } finally {

                // 对分布式锁解锁.直接解锁即可.
                // TODO: 如果这样做会出现死锁的情况.
                // 虽然解决大部分只从DB中查询一次的情况,但是很有可能会出现死锁的情况.
                // 比如当前线程加锁成功,但是执行的时候突然闪断，造成没有任何线程对该所进行释放导致所有用户都在等待该锁释放而造成死锁现象.
                // 那么我们就在加锁的时候给该锁加上指定的过期时间.
                // 即使当前服务宕机,那么依然可以在过期时间之后进行锁的删除.
                // TODO: 为什么删锁需要原子性.
                /*
                 * --------------------------------------------------------
                 *      删锁需要原子性是因为，当前客户端删除的锁不是自己上的锁，而是别人上的
                 * 锁.
                 *      解决办法是用lua脚本进行原子性的删除锁.
                 * --------------------------------------------------------
                 * */

                // stringRedisTemplate.delete("lock");
                String lua = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                        "then\n" +
                        "    return redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
                Long execute = stringRedisTemplate.execute(new DefaultRedisScript<Long>(lua, Long.class), Arrays.asList("lock"),
                        uuid);
                // 解锁成功返回1,否则返回0.
                System.out.println(execute);

            }

        } else {

            // 表示占锁失败: 触发自旋操作
            // 如果递归调用操作太快很有可能触发栈空间不足的异常.
            try {

                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.t1DBLock(id);
        }

        return result;
    }

    public String getDataFromDB(Long id) {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String result = "";
        String cache = ops.get("test:" + id);
        // 如果加锁,是不是效率就会变得更低了?
        // 我们要明白synchronized锁什么时候释放.
        if (org.springframework.util.StringUtils.isEmpty(cache)) {

            // 说明没有缓存
            // 这里应该只会出现一次
            System.out.println("从DB中读取了数据...");
            if (id < 10) {

                // 说明数据库里面有东西.
                // 将从DB中查询出来的结果放入缓存中.
                // 这里查询出来的数据是正常数据.
                Double random = Math.random() * 10 + 7;
                long l = random.longValue();
                // 放入缓存中.
                ops.set("test:" + id, id.toString(), l, TimeUnit.DAYS);
                result = id.toString();
            } else {

                // TODO: 解决缓存穿透对null结果进行缓存.
                // 查询出来之后,在存入缓存中将该商品.
                // 为了较快的看出成果,这里将Key的有效期设置为半分钟.
                // 30秒之后立即删除.
                // 为过期时间设置一个随机数.
                // 这样获取到的数是0-1的.
                Double random = Math.random() * 10 + 30;
                // 解决了缓存雪崩问题,整体缓存Key的有效期在30-40S之间.
                long time = random.longValue();
                ops.set("test:" + id, "null", time, TimeUnit.SECONDS);
                result = "null";
            }
        } else {

            // 表名缓存击穿情况下缓存中存在的情况下.
            result = cache;
        }

        return result;
    }

    /**
     * 只要已从DB中读取数据,那么就先锁住, 即使高并发环境下所有的用户将会竞争锁.
     *
     * @param id
     * @return
     */
    public String t1DB(Long id) {

        // TODO: 缓存击穿通过加锁来解决，就是为了避免过多的用户从DB中查询，因为我们知道他们查询的数据是一样的，
        //  所以我们只需要让一个用户查询返回我们将其存入缓存中供其他用户使用即可.
        // 既然要用锁,那么应该锁谁?
        String result = "";
        // 该解锁一定是针对热点数据进行加锁的.
        synchronized (lock) {
            // 只要用户已拿到锁，就先判断自己需要的数据是否在缓存中存在,如果存在就直接返回，如果不存在那么就从DB中获取.
            // 这里再从缓存中判断一边是因为，万一有个用户先抢到锁进行查询的同时有一个用户在等待所，当第一个用户查询到数据之后，放入缓存中，
            // 第二个用户还认为缓存中没有，那么仍然会从DB中查询.

            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            String cache = ops.get("test:" + id);
            // 如果加锁,是不是效率就会变得更低了?
            // 我们要明白synchronized锁什么时候释放.
            if (org.springframework.util.StringUtils.isEmpty(cache)) {

                // 说明没有缓存
                // 这里应该只会出现一次
                System.out.println("从DB中读取了数据...");
                if (id < 10) {

                    // 说明数据库里面有东西.
                    // 将从DB中查询出来的结果放入缓存中.
                    // 这里查询出来的数据是正常数据.
                    Double random = Math.random() * 10 + 7;
                    long l = random.longValue();
                    // 放入缓存中.
                    ops.set("test:" + id, id.toString(), l, TimeUnit.DAYS);
                    result = id.toString();
                } else {

                    // TODO: 解决缓存穿透对null结果进行缓存.
                    // 查询出来之后,在存入缓存中将该商品.
                    // 为了较快的看出成果,这里将Key的有效期设置为半分钟.
                    // 30秒之后立即删除.
                    // 为过期时间设置一个随机数.
                    // 这样获取到的数是0-1的.
                    Double random = Math.random() * 10 + 30;
                    // 解决了缓存雪崩问题,整体缓存Key的有效期在30-40S之间.
                    long time = random.longValue();
                    ops.set("test:" + id, "null", time, TimeUnit.SECONDS);
                    result = "null";
                }

            } else {

                // 表名缓存击穿情况下缓存中存在的情况下.
                result = cache;
            }

        }

        return result;
    }

}
