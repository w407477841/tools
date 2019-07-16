package com.xywg.iot.modular.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.modular.app.model.AppInfo;
import com.xywg.iot.modular.app.dao.AppInfoMapper;
import com.xywg.iot.modular.app.service.IAppInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.xycache.annotations.OpenCache;
import com.xywg.iot.xycache.annotations.RemoveCache;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyifei
 * @since 2019-01-16
 */
@Service
public class AppInfoServiceImpl extends ServiceImpl<AppInfoMapper, AppInfo> implements IAppInfoService {

    @Override
    @OpenCache(model = "app",exp = 600 ,clazz = AppInfo.class)
    public AppInfo selectCacheOne(String key) {

        Wrapper wrapper  =new EntityWrapper();
        wrapper.eq("app_key",key);
        wrapper.eq("is_del",0);
        return this.selectOne(wrapper);
    }

    @Override
    @RemoveCache(model = "app")
    public void removeCaches(String key) {

    }
}
