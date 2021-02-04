package com.ww.mall.tiny.comom.exception;

import java.lang.annotation.*;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-19 14:42
 * @describe:   标识方法  前面在遇到用户收发短信service方法，就要抛出异常。
 *                      因为短信都没有存入redis中，校验短信的时候一定会有问题。不像获取用户权限，redis宕机了，不影响业务。
 */

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CatchException {
}
