package com.xywg.iot.modular.remote.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfigRecord;
import com.xywg.iot.modular.remote.model.vo.DeviceRemoteConfigRecordVO;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2019-01-15
 */
public interface DeviceRemoteConfigRecordMapper extends BaseMapper<DeviceRemoteConfigRecord> {

    /**
     * 获取设备列表
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<DeviceInfoVo> selectDevicePageList(RowBounds rowBounds, @Param("ew") Wrapper<DeviceUpgradePackage> wrapper);


    /**
     * 获取设备配置履历
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<DeviceRemoteConfigRecordVO> getDeviceConfigHistoryPage(RowBounds rowBounds, @Param("ew") Wrapper<DeviceRemoteConfigRecordVO> wrapper);


    /**
     * 获取设备配置履历
     * @param wrapper
     * @return
     */
    List<DeviceRemoteConfigRecordVO> getDeviceConfigHistory(@Param("ew") Wrapper<DeviceRemoteConfigRecordVO> wrapper);
}
