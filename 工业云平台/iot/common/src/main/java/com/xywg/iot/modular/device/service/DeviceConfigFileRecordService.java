package com.xywg.iot.modular.device.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.modular.device.model.DeviceConfigFileRecord;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
public interface DeviceConfigFileRecordService extends IService<DeviceConfigFileRecord> {

  /*  *//**
     * 根据条件获取升级履历详细信息
     * @param dto
     * @return
     *//*
   List<DeviceUpgradeRecordData> getDeviceUpgradeRecordList(QueryWhereDto<DeviceUpgradeRecord> dto);

    *//**
     * 根据条件获取升级履历详细信息
     * @param wrapper
     * @return
     *//*
    List<DeviceUpgradeRecordData> getDeviceUpgradeRecordList(Wrapper<DeviceUpgradeRecord> wrapper);*/

}
