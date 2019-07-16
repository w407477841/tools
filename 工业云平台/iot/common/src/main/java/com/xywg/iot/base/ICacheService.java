package com.xywg.iot.base;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:47 2018/12/24
 * Modified By : wangyifei
 */
public interface ICacheService<T>  {
    /**
     * 查询缓存，自己实现内部逻辑
     * 缓存的key =  app:model:key
     * 如 app = iot , model = user
     * @param key
     * @return
     */
    T selectCacheOne(String key );

    /**
     * 删除缓存   以,分割多个
     * @param key
     */
    void removeCaches(String key);


}
