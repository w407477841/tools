package com.xywg.iot.modular.colud.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.modular.device.vo.DevicePropertyRecordVo;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/8
 *TIME:21:05
 */
public interface DevicePropertyRecordService {
    /**
     * 运行情况
     * @param page
     * @param devicePropertyRecordVo
     * @return
     */
    List<DevicePropertyRecordVo> getRunCondition(Page<DevicePropertyRecordVo> page, DevicePropertyRecordVo devicePropertyRecordVo);
}
