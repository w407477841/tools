package com.xywg.iot.modular.device.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.modular.device.dto.DeviceEventRecordDTO;
import com.xywg.iot.modular.device.dto.DeviceMethodRecordDTO;
import com.xywg.iot.modular.device.model.DeviceEventRecord;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2018-12-11
 */
public interface IDeviceEventRecordService extends IService<DeviceEventRecord> {

    /**
     *  运行数据
     * @param page
     * @param dto
     * @return
     */
    void getRunCondition(Page<Map<String,Object>> page, DeviceEventRecordDTO dto);

}
