package com.xywg.iot.modular.upgrade.service;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecord;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecordData;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
public interface IDeviceUpgradeRecordService extends IService<DeviceUpgradeRecord> {

    /**
     * 根据条件获取升级履历详细信息
     * @param dto
     * @return
     */
   List<DeviceUpgradeRecordData> getDeviceUpgradeRecordList(QueryWhereDto<DeviceUpgradeRecord> dto);

    /**
     * 根据条件获取升级履历详细信息
     * @param wrapper
     * @return
     */
    List<DeviceUpgradeRecordData> getDeviceUpgradeRecordList(Wrapper<DeviceUpgradeRecord> wrapper);

}
