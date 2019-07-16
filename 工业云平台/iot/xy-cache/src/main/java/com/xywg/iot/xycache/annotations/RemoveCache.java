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
public @interface RemoveCache {
    /**
     *  缓存KEY
     *  默认redis
     * @return
     */
    String key() default "#key";

    String model() ;

    /**
     *
     * @return
     */
    String app() default "iot";

}
