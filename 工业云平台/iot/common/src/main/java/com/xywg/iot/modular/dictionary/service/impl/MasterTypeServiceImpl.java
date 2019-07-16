package com.xywg.iot.modular.dictionary.service.impl;

import com.xywg.iot.xycache.annotations.OpenCache;
import com.xywg.iot.xycache.annotations.RemoveCache;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.dictionary.model.MasterType;
import com.xywg.iot.modular.dictionary.dao.MasterTypeMapper;
import com.xywg.iot.modular.dictionary.service.IMasterTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jxj
 * @since 2018-12-26
 */
@Service
public class MasterTypeServiceImpl extends ServiceImpl<MasterTypeMapper, MasterType> implements IMasterTypeService {
    /**
     *  根据ID 查询 产品类别
     * @param key
     * @return
     */
    @Override
    @OpenCache(exp = 500, model = "mastertype", clazz = MasterType.class)
    public MasterType selectCacheOne(String key) {
        return selectById(Integer.parseInt(key));
    }

    @Override
    @RemoveCache(model = "mastertype")
    public void removeCaches(String key) {

    }
}
