package com.xywg.iot.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.device.model.DeviceConfigFile;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author z
 * @since 2019-03-27
 */
public interface IDeviceConfigFileService extends IService<DeviceConfigFile> {

    /**
     * 获取设备列表
     * @param dto
     * @return
     */
    ResultPage<DeviceInfoVo> selectDevicePageList(QueryWhereDto<DeviceConfigFile> dto);

}
