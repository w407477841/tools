package com.xywg.iot.modular.device.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modular.device.dao.DeviceConfigFileRecordMapper;
import com.xywg.iot.modular.device.model.DeviceConfigFileRecord;
import com.xywg.iot.modular.device.service.DeviceConfigFileRecordService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
@Service
public class DeviceConfigFileRecordServiceImpl extends ServiceImpl<DeviceConfigFileRecordMapper, DeviceConfigFileRecord> implements DeviceConfigFileRecordService {

  /*  @Override
    public List<DeviceUpgradeRecordData> getDeviceUpgradeRecordList(QueryWhereDto<DeviceUpgradeRecord> dto) {
        DeviceUpgradeRecord deviceUpgradeRecord = dto.getBody();
        EntityWrapper<DeviceUpgradeRecord> ew = new EntityWrapper<>();
        if (deviceUpgradeRecord != null) {
            if (deviceUpgradeRecord.getPackageId() != null) {
                ew.eq("a.package_id", deviceUpgradeRecord.getPackageId());
            }
        }
        return baseMapper.getDeviceUpgradeRecordList(ew);
    }

    @Override
    public List<DeviceUpgradeRecordData> getDeviceUpgradeRecordList(Wrapper<DeviceUpgradeRecord> wrapper) {
        return baseMapper.getDeviceUpgradeRecordList(wrapper);
    }*/
}
