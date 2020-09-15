package com.mzx.gulimall.product.aspect;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.product.annotation.GuliMallCache;
import com.mzx.gulimall.util.CurrentStringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 自定义切面类:
 *
 * @author ZhenXinMa.
 * @slogan 滴水穿石, 不是力量大, 而是功夫深.
 * @date 2020/9/9 21:37.
 */
@Aspect
@Component
public class GuliMallCacheAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private RedissonClient redissonClient;

    /*
     * --------------------------------------------------------
     * 根据功能实现需求自定义切面的要求.
     * 1. 前置通知
     * 2. 后置通知
     * 3. 最终通知
     * 4. 环绕通知
     * 5. 异常通知
     * 根据分布式下缓存一致性,我们需要使用环绕通知.
     * 既然需要使用AOP,那么就应该先来复习下AOP的基本概念.
     * 1. Join point 连接点: 表示程序执行的一个点, 在Spring理念中应该是一个方法.
     * 2. pointcut 切入点: 与连接点相关, 切入点所就是连接点的一个有穷集合.
     * 3. Advice 增强: 在连接点执行操作之前,需要做的增强操作. 一共有五种增强.
     *    也就是上面的五种增强方法.
     * 4. Target Object 目标对象,既织入五种增强的目标对象,也就是被执行的方法.
     * 5. AOP Proxy  代理类: 一个AOP就是一个JDK代理或者CGLIB代理.
     * 6. Weaving 织入: 将当前的Aspect应用到目标方法中.
     * --------------------------------------------------------
     * */

    /*
     * --------------------------------------------------------
     * 实现AOP方法需要注意的事项:
     * 1. 方法必须有一个Object类型的返回值.
     * 2. 方法必须提供一个ProceedingJoinPoint 连接点通知.
     * 3. 方法必须抛出一个异常: Throwable异常.
     * 4. 需要手动执行目标方法. 通过参数连接点执行proceed()来执行目标方法.
     * --------------------------------------------------------
     * */

    /**
     * 切带指定注解的切入点表达式.
     * <p>
     * 只要在要在缓存中缓存数据的方法中添加注解GuliMallCache,那么就应该会自动进入该切面.
     * <p>
     * 缓存的key应该包含查询参数.
     * 分布式锁的Key应该是前缀在加上商品的ID也就是四查询参数。
     * <p>
     * 假如说现在缓存的key是用户指定的前缀和参数(如果是多个参数,取出第一个参数来进行保存,如果没有参数,那么就使用前缀进行保存. )
     *
     * @return
     */
    @Around(value = "@annotation(com.mzx.gulimall.product.annotation.GuliMallCache)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        System.out.println("@GuliMallCache 环绕通知执行了. ");
        // TODO: 正在完成...
        // 从point获取必要的信息.
        // 要执行的切入点的参数,也就是要执行的方法的参数.
        // 1. 获取方法参数.
        Object[] args = point.getArgs();
        // 1.1 或的参数之后,应该先从缓存中查看是否存在当前要查询的数据,如果存在,那么就直接返回,如果不存在,则从DB中查询,
        //     从DB中查询的,如果从DB中查询,那么就久应该加锁,为了解决缓存击穿现象.
        // 2. 获取方法引用.
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 我们获取到的这个方法的返回值之后,我们用来做什么?
        // 在我们获取到之后,我们从缓存中获取到JSON数据之后,利用该返回值还原原生存入缓存中的对象进行返回.
        // 这样不需要对返回值类型进行一一判断而再次进行返回.
        // 我们需要注意的是方法返回值也是一个类型,所以说也就是一个class.
        Class returnType = signature.getReturnType();
        Method method = signature.getMethod();
        // 现在需要从该注解中的属性上获取当前切入点的要执行缓存的一些要求.
        GuliMallCache mallCache = method.getAnnotation(GuliMallCache.class);
        // 3. 开始正常处理.
        // 3.1 先对前缀进行处理.
        String prefix = mallCache.prefix();
        String lockKey = mallCache.lock();
        if (!prefix.endsWith(":")) {

            // 在使用@GuliMallCache注解的时候指定前缀属性没有以:结束.
            // 那么就在这里默认添加.
            prefix += ":";

        }

        String key = mallCache.key();
        int timeout = mallCache.timeout();
        int random = mallCache.random();
        TimeUnit unit = mallCache.unit();
        // cacheKey就是要从数据库中查询缓存的key.
        String cacheKey = this.getCacheKey(args, key, prefix);
        // 先从缓存中查询一下数据.
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String s = ops.get(cacheKey);
        if (StringUtils.isEmpty(s)) {

            System.out.println("缓存未命中 多个线程开始获取可重入的分布式锁 当前线程: " + Thread.currentThread().getId());
            // 如果缓存中查询的数据为空,那么就查询.
            // 而查询就是直接调用该方法.
            // 但是为了防止缓存击穿(就是在同一时刻,大量的key失效,并且这一时刻又有大量请求进行访问)会导致数据库压力巨增.
            // 为了杜绝这种情况,采用加锁解决.
            // 默认获取的锁和Java中的ReentrantLock是一样的.
            RLock lock = redissonClient.getLock(lockKey);
            // 使用默认的就行, 其内部使用了看门狗原理.
            // 快捷键: Ctrl + Alt + 左键单击可以直接进入giant调用该方法内部查看源代码.
            lock.lock();
            try {

                String cacheContent = ops.get(cacheKey);
                // 同步代码块
                // 每次当多个用户同时进来之后为了防止多个用户去访问DB,那么我在获取到锁之后应该立马去缓存中查看.
                if (StringUtils.isEmpty(cacheContent)) {

                    // 当前对注解的处理已经确保了缓存雪崩以及缓存穿透和缓存击穿,并且针对缓存击穿,进一步的进行了优化,
                    // 保证了就算发生了缓存击穿, 在这一个时刻也只允许一个线程从数据库中查询,并且其他用户再次查询的时候
                    // 会再次从缓存中查询一下,如果查询的还是空值,那么就允许该线程访问DB.
                    System.out.println("线程: " + Thread.currentThread().getId() + "获取到了可重入的分布式锁.");
                    // 调用方法,并且将量结果存入缓存中.
                    Object proceed = point.proceed(args);
                    String string = JSON.toJSONString(proceed);
                    // 放入缓存中.
                    // 过期时间是有用户自己定义的.
                    ops.set(cacheKey, string, timeout + random, unit);
                    return proceed;

                } else {

                    System.out.println("线程: " + Thread.currentThread().getId() + "虽然获取了锁,但是已经有一个线程优先于" +
                            "从DB中查询并且放入了缓存,当前线程不用从缓存中查询. ");
                    return JSON.parseObject(cacheContent, returnType);

                }

            } catch (Exception e) {

                e.printStackTrace();
                throw new RuntimeException("使用自定义注解: @GuliMallCache 出现异常: " + e.getMessage());

            } finally {

                lock.unlock();
            }

        } else {

            System.out.println("缓存命中,直接从缓存中查询并且返回. ");
            return JSON.parseObject(s, returnType);

        }

    }


    private String getCacheKey(Object[] args, String key, String prefix) {

        String cacheKey = "";
        // 获取cacheKey.
        if (args == null || args.length <= 0) {

            if (!StringUtils.isEmpty(key)) {

                // 表示处理当前方法只是缓存一类数据.
                cacheKey = CurrentStringUtils.append(new StringBuilder(), prefix, key);

            } else {

                // cacheKey可能为空吗? 可能为空,那么我就抛出异常.
                throw new RuntimeException("注解@GuliMallCache中属性key不能为空在没有参数的情况下. ");

            }

        } else {

            // 针对类似缓存商品根据SKU的ID.
            // 需要取出
            cacheKey = CurrentStringUtils.append(new StringBuilder(), prefix, args[0].toString());

        }

        return cacheKey;

    }

}
