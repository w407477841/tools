package com.xywg.iot.modular.upgrade.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecord;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecordData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
public interface DeviceUpgradeRecordMapper extends BaseMapper<DeviceUpgradeRecord> {

    List<DeviceUpgradeRecordData> getDeviceUpgradeRecordList(@Param("ew") Wrapper<DeviceUpgradeRecord> wrapper);

}
