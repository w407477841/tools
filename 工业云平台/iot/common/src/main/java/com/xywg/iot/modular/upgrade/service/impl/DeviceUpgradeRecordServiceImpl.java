package com.xywg.iot.modular.upgrade.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.modular.upgrade.dao.DeviceUpgradeRecordMapper;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecord;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecordData;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradeRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
@Service
public class DeviceUpgradeRecordServiceImpl extends ServiceImpl<DeviceUpgradeRecordMapper, DeviceUpgradeRecord> implements IDeviceUpgradeRecordService {

    @Override
    public List<DeviceUpgradeRecordData> getDeviceUpgradeRecordList(QueryWhereDto<DeviceUpgradeRecord> dto) {
        DeviceUpgradeRecord deviceUpgradeRecord = dto.getBody();
        EntityWrapper<DeviceUpgradeRecord> ew = new EntityWrapper<>();
        if (deviceUpgradeRecord != null) {
            if (deviceUpgradeRecord.getPackageId() != null) {
                ew.eq("a.package_id", deviceUpgradeRecord.getPackageId());
                ew.orderBy("a.create_time",true);
            }
        }
        return baseMapper.getDeviceUpgradeRecordList(ew);
    }

    @Override
    public List<DeviceUpgradeRecordData> getDeviceUpgradeRecordList(Wrapper<DeviceUpgradeRecord> wrapper) {
        return baseMapper.getDeviceUpgradeRecordList(wrapper);
    }
}
