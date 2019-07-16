package com.xywg.iot.modular.device.service;

import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.device.model.DeviceErrorLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author z
 * @since 2019-03-01
 */
public interface IDeviceErrorLogService extends IService<DeviceErrorLog> {

    /**
     * 带条件查询列表
     * @param dto
     * @return
     */
    ResultPage<DeviceErrorLog> selectErrorLogPageList(QueryWhereDto<DeviceErrorLog> dto);

}
