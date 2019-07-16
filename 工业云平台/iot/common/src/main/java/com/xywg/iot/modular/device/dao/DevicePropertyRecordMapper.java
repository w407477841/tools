package com.xywg.iot.modular.device.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.modular.device.model.DevicePropertyRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.iot.modular.device.vo.DevicePropertyRecordVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangyf
 * @since 2019-01-02
 */
public interface DevicePropertyRecordMapper extends BaseMapper<DevicePropertyRecord> {
    /**
     * 运行情况
     * @param page
     * @param map
     * @return
     */
    List<DevicePropertyRecordVo> getRunCondition(Page<DevicePropertyRecordVo> page, Map<String,Object> map);
}
