package com.mzx.gulimall.cart.aspect;

import com.mzx.gulimall.util.CurrentStringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 计算一个方法运行耗费了多长时间注解的切面类.
 *
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-27 19:46 周日.
 */
@Aspect
@Component
public class CalculationPeriodAspect {

    /**
     * 计算一个方法运行耗费了多长时间注解的环绕通知.
     *当前正在执行的方法: org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint$MethodSignatureImpl t1
     * @CalculationPeriod效果测试: 1203
     * @param point
     * @return
     * @throws Throwable
     */
    @Around(value = "@annotation(com.mzx.gulimall.cart.annotation.CalculationPeriod)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String className = methodSignature.getClass().getName();
        String methodName = methodSignature.getMethod().getName();
        String cn = CurrentStringUtils.append(new StringBuilder(), className, " ", methodName);
        System.out.println("当前正在执行的方法: " + cn);
        // 获取参数.
        Object[] args = point.getArgs();
        long start = System.currentTimeMillis();
        Object returnObject = point.proceed(args);
        long end = System.currentTimeMillis();
        System.out.println("@CalculationPeriod效果测试: " + (end - start));
        return returnObject;

    }


}
