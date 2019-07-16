package com.xywg.iot.xycache.annotations;


import java.lang.annotation.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:40 2018/11/7
 * Modified By : wangyifei
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenCache {
    /**
     *  缓存KEY
     *  默认redis
     * @return
     */
    String key() default "#key";

    /**
     *  过期时间 单位 秒
     *  默认 无限期
     *
     * @return
     */
    long  exp() default     -1;

    /**
     * 模块名
     * @return
     */
    String model() ;


    /**
     * 项目明细
     * @return
     */
    String app() default "iot" ;

    Class clazz() ;

}
