package com.xywg.iot.modular.remote.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.remote.dao.DeviceRemoteConfigMapper;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfig;
import com.xywg.iot.modular.remote.model.vo.DeviceRemoteConfigVO;
import com.xywg.iot.modular.remote.service.DeviceRemoteConfigService;
import com.xywg.iot.util.UserUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author z
 * @since 2019-01-17
 */
@Service
public class DeviceRemoteConfigServiceImpl extends ServiceImpl<DeviceRemoteConfigMapper, DeviceRemoteConfig> implements DeviceRemoteConfigService {

    @Override
    public ResultPage<DeviceRemoteConfigVO> selectConfigPageList(QueryWhereDto<DeviceRemoteConfig> dto) {
        Page<DeviceRemoteConfigVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        EntityWrapper<DeviceRemoteConfigVO> ew = new EntityWrapper<>();
        ew.orderBy("a.create_time",false);
        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            ew.eq("a.create_user", UserUtil.USERID.get());
        }
        List<DeviceRemoteConfigVO> list = baseMapper.selectConfigPageList(page, ew);
        return new ResultPage<>(page.getTotal(), list);
    }
}
