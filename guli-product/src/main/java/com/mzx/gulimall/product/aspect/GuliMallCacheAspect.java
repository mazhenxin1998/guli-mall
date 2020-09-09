package com.mzx.gulimall.product.aspect;

import com.mzx.gulimall.product.annotation.GuliMallCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

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
     *
     * @return
     */
    @Around(value = "@annotation(com.mzx.gulimall.product.annotation.GuliMallCache)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        // 从point获取必要的信息.
        // 要执行的切入点的参数,也就是要执行的方法的参数.
        // 1. 获取方法参数.
        Object[] args = point.getArgs();
        System.out.println("执行方法的参数为: " + args.toString());
        // 2. 获取方法返回类型.
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 我们获取到的这个方法的返回值之后,我们用来做什么?
        // 在我们获取到之后,我们从缓存中获取到JSON数据之后,利用该返回值还原原生存入缓存中的对象进行返回.
        // 这样不需要对返回值类型进行一一判断而再次进行返回.
        // 我们需要注意的是方法返回值也是一个类型,所以说也就是一个class.
        Class returnType = signature.getReturnType();
        // 3. 获取方法引用.
        Method method = signature.getMethod();
        // 现在需要从该注解中的属性上获取当前切入点的要执行缓存的一些要求.
        GuliMallCache mallCache = method.getAnnotation(GuliMallCache.class);
//        String cache = "";
//        // 这样处理之后,就会将JSON
//        if (StringUtils.isEmpty(cache)) {
//
//            return JSON.parseObject(cache, returnType);
//
//        }


        // 参数的获取.
        System.out.println("切入表达式执行了");

        // 前置通知.

        // 目标方法执行.
        Object returnResult = point.proceed(args);
        // 通过方法返回类型,和Object结果,将其还原成原生方法执行之后的结果进行返回.

        // 后置通知.


        return returnResult;
    }


}
