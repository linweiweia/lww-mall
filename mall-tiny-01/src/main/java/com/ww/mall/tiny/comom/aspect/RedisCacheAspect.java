package com.ww.mall.tiny.comom.aspect;

import com.ww.mall.tiny.comom.exception.CatchException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-19 13:56
 * @describe:   redis异常统一处理类
 *              写一次try catch就够，不用在操作redis相关的service写try catch
 *              redis宕机了，程序还可以跑，不至于程序直接挂掉。
 */

@Aspect
@Component
@Order(2)   //多个切面的执行顺序 值越小优先级越高
public class RedisCacheAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAspect.class);

    //配置切面
    @Pointcut("execution(public * com.ww.mall.tiny.service.*CacheService.*(..)) || execution(public * com.ww.mall.tiny.service.UmsMembersService.*(..))")
    public void cacheAspect() {}

    //配置环绕通知
    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        //如果CatchException注解的方法就要抛出异常（短息业务，如果redis宕机了要返回错误）
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Object result = null;
        try{
            //手动执行代码
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            //如果包含CatchException就抛出异常（短信业务）  get redis才抛出throwable  有点问题
            if (method.isAnnotationPresent(CatchException.class)) {
                LOGGER.info("catchException:{}",throwable.getMessage());
                throw throwable;
            } else {
                //如果没有包含CatchException就不用抛出，程序继续执行（获取用户信息业务）
                LOGGER.error(throwable.getMessage());
            }
        }
        return result;
    }
}
