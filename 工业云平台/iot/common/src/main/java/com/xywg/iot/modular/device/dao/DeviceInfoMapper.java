package com.xywg.iot.modular.device.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xywg.iot.modular.device.vo.DeviceAddrVO;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jxj
 * @since 2019-01-04
 */
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

    List<DeviceAddrVO> deviceAddr(@Param("createUser") String user);

    /**
     * 获取列表
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<DeviceInfoVo> selectDevice(Page<DeviceInfoVo> page,Map<String, Object> map) throws Exception;

    /**
     * 单条查询
     * @param id
     * @return
     * @throws Exception
     */
    DeviceInfo selectDeviceById(Integer id) throws Exception;
}
