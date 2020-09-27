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
     * 当前正在执行的方法: org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint$MethodSignatureImpl t1
     *
     * @param point
     * @return
     * @throws Throwable
     * @CalculationPeriod效果测试: 1203
     */
    @Around(value = "@annotation(com.mzx.gulimall.cart.annotation.CalculationPeriod)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Object[] args = point.getArgs();
        String methodName = this.getMethodName(point);
        long start = System.currentTimeMillis();
        // 方法执行前是前置通知.
        Object returnObject = point.proceed(args);
        // 方法执行后是后置通知.
        long end = System.currentTimeMillis();
        System.out.println(methodName + " 运行时长: " + (end - start) + "毫秒.");
        return returnObject;

    }

    private String getMethodName(ProceedingJoinPoint point) {

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String className = point.getTarget().getClass().getName();
        String methodName = methodSignature.getMethod().getName();
        Object[] args = point.getArgs();
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {

            CurrentStringUtils.append(builder, arg.toString());

        }
        String cn = CurrentStringUtils.append(new StringBuilder(), className, " ",
                methodName, "(", builder.toString(), ")");
        return cn;

    }


}
