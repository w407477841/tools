package com.wyf.common.commonredis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:35 2019/4/8
 * Modified By : wangyifei
 */
@Target({ElementType.METHOD})
@Documented
public @interface RedisDatabase {
}
