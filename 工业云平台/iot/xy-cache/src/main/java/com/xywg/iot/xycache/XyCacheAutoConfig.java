package com.xywg.iot.xycache;

import cn.hutool.json.JSONUtil;
import com.xywg.iot.xycache.annotations.OpenCache;
import com.xywg.iot.xycache.annotations.RemoveCache;
import com.xywg.iot.xycache.utils.BaseRedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:44 2019/3/8
 * Modified By : wangyifei
 */
@Configuration
@ConditionalOnProperty(prefix = "xywg.cache",value = "enabled" , matchIfMissing = true)
public class XyCacheAutoConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CahceAop.class);

    private static final  String executionSearch = "@annotation(openCache)" ;

    private static final  String executionRemove = "@annotation(removeCache)" ;
    @Component
    @Aspect
    public class CahceAop {



        @Autowired
        private BaseRedisUtil redisUtil;

        public CahceAop() {
            System.out.println("----------------------------------------------");
            System.out.println("--------------------缓存启动------------------");
            System.out.println("----------------------------------------------");
        }

        /**
         * 查询缓存,返回缓存/数据库
         * @param pjp
         * @param openCache
         * @return
         */
        @Around(value = executionSearch)
        public Object searchAdvice(ProceedingJoinPoint pjp, OpenCache openCache) throws Throwable {
            Method m = ((MethodSignature) pjp.getSignature()).getMethod();
            // 获取方法对象
            Method methodWithAnnotations = pjp.getTarget().getClass().getDeclaredMethod(pjp.getSignature().getName(), m.getParameterTypes());
            //方法参数
            Object[] as = pjp.getArgs();
            // key表达式
            String keyExpr = openCache.key();
            // key
            String parseKey  =  parseKey(methodWithAnnotations,as,keyExpr);
            String key = openCache.app()+":"+openCache.model()+":"+parseKey;
            String data = (String) redisUtil.get(key);
            if(data == null){
                LOGGER.info("走数据库<{}>",key);
                //执行代码逻辑
                Object result = pjp.proceed();
                if(result == null){
                    return result ;
                }
                long exp = openCache.exp() ;
                if(exp <= 0){
                    redisUtil.set(key,JSONUtil.toJsonStr(result));
                }else{
                    redisUtil.set(key,JSONUtil.toJsonStr(result),exp);
                }
                return result;
            }
            LOGGER.info("走缓存<{}>",key);
            return JSONUtil.toBean(data,openCache.clazz());
        }


        /**
         *  删除 ,为了方便还是不要用这个了
         * @param pjp
         * @param removeCache
         * @return
         */
        @Around(value = executionRemove)
        public Object removeAdvice(ProceedingJoinPoint pjp, RemoveCache removeCache)throws Throwable  {

            Method m = ((MethodSignature) pjp.getSignature()).getMethod();
            // 获取方法对象
            Method methodWithAnnotations = pjp.getTarget().getClass().getDeclaredMethod(pjp.getSignature().getName(), m.getParameterTypes());
            //  方法参数
            Object[] as = pjp.getArgs();
            // key表达式
            String keyExpr = removeCache.key();
            // key
            String[] parseKeys  =  parseKey(methodWithAnnotations,as,keyExpr).split(",");

            String keys[] = new String[parseKeys.length];
            for(int i = 0 ; i<parseKeys.length ; i++){
                keys[i] = removeCache.app()+":"+removeCache.model()+":"+parseKeys[i];
            }
            LOGGER.info("清缓存<{}>",Arrays.toString(keys));
            redisUtil.remove(keys);
            return   pjp.proceed();
        }





        /**
         *
         * @param method 执行的方法
         * @param argValues 参数
         * @param expr 表达式  目前不支持 对象.属性
         *              #key 表示  （缓存key = 参数名为key的值）
         *              如果不含# 则直接是缓存key
         * @return
         */
        private String parseKey(Method method, Object[] argValues, String expr) {
            if (expr.contains("#")) {
                String paramName = expr.substring(expr.indexOf('#') + 1);
                // 获取方法参数名列表
                LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
                String[] paramNames = discoverer.getParameterNames(method);
                for (int i = 0; i < paramNames.length; i++) {
                    if (paramNames[i].equals(paramName)) {
                        // abc#abc 且 abc = def 则 返回 abcdef
                        return expr.substring(0, expr.indexOf('#')) + argValues[i].toString();
                    }
                }
                throw new IllegalArgumentException("解析不了该参数，错误参数表达式");
            } else {
                // 不需要解析，直接返回
                return expr;
            }
        }


    }


}
