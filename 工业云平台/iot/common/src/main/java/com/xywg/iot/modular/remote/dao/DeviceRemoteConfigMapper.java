package com.xywg.iot.modular.remote.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfig;
import com.xywg.iot.modular.remote.model.vo.DeviceRemoteConfigVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author z
 * @since 2019-01-17
 */
public interface DeviceRemoteConfigMapper extends BaseMapper<DeviceRemoteConfig> {

    /**
     * 获取设备列表
     * @param rowBounds
     * @param wrapper
     * @return
     */
    List<DeviceRemoteConfigVO> selectConfigPageList(RowBounds rowBounds, @Param("ew") Wrapper<DeviceRemoteConfigVO> wrapper);
}
