package com.xywg.iot.modular.device.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.modular.device.dto.DeviceMethodRecordDTO;
import com.xywg.iot.modular.device.model.DeviceMethodRecord;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.modular.device.vo.DevicePropertyRecordVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2018-12-11
 */
public interface IDeviceMethodRecordService extends IService<DeviceMethodRecord> {

    /**
     *  运行数据
     * @param page
     * @param dto
     * @return
     */
   void getRunCondition(Page<Map<String,Object>> page, DeviceMethodRecordDTO dto);

}
