package com.xywg.iot.modular.cloud.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.xycache.annotations.OpenCache;
import com.xywg.iot.xycache.annotations.RemoveCache;
import com.xywg.iot.modular.cloud.model.CloudUser;
import com.xywg.iot.modular.cloud.dao.CloudUserMapper;
import com.xywg.iot.modular.cloud.service.ICloudUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyf
 * @since 2018-12-19
 */
@Service
public class CloudUserServiceImpl extends ServiceImpl<CloudUserMapper, CloudUser> implements ICloudUserService  {

    @Override
    @OpenCache(exp = 300,model = "user",clazz = CloudUser.class)
    public CloudUser selectCacheOne( String key) {
        Wrapper<CloudUser> wrapper = new EntityWrapper();
        wrapper.eq("username", key);
        return super.selectOne(wrapper);
    }

    @Override
    @RemoveCache(model = "user")
    public void removeCaches(String key) {

    }
}
