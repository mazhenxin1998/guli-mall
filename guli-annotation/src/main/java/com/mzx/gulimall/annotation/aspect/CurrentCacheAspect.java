package com.mzx.gulimall.annotation.aspect;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.annotation.CurrentCache;
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
 * 在当前服务中不需要对Redisson进行配置,因为当前服务迟早会被引用到其他服务,而当前服务的配置类也就是RedissonConfig也将会一起被
 * 引入到别的服务中，这个时候两个配置类将会发生冲突. 所以说在该模块中只需要引入该依赖就行,我们在该服务中只需要使用其提供的顶层接口,
 * 至于其他怎么处理，在当前服务被引入到别的服务的时候会自动注入的.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-25 21:09 周五.
 */
@Aspect
@Component
public class CurrentCacheAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private RedissonClient redissonClient;

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
     * @return 环绕通知返回的方法执行后的结果.
     */
    @Around(value = "@annotation(com.mzx.gulimall.annotation.CurrentCache)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        System.out.println("@CurrentCache 环绕通知执行了. ");
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
        CurrentCache mallCache = method.getAnnotation(CurrentCache.class);
        // 3. 开始正常处理.
        // 3.1 先对前缀进行处理.
        String prefix = mallCache.prefix();
        String lockKey = mallCache.lock();
        if (!prefix.endsWith(":")) {

            // 如果注解中的属性没有以:结尾,那么就添加.
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
            // 需要取出.
            cacheKey = CurrentStringUtils.append(new StringBuilder(), prefix, args[0].toString());

        }

        return cacheKey;

    }

}
