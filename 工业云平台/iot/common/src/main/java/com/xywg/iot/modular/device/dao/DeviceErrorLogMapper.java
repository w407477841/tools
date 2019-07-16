package com.xywg.iot.modular.device.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.iot.modular.device.model.DeviceErrorLog;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author z
 * @since 2019-03-01
 */
public interface DeviceErrorLogMapper extends BaseMapper<DeviceErrorLog> {

   /* *//**
     * 获取设备列表
     * @param rowBounds
     * @param wrapper
     * @return
     *//*
    List<DeviceErrorLog> selectErrorLogPageList(RowBounds rowBounds, @Param("ew") Wrapper<DeviceErrorLog> wrapper);*/

}
