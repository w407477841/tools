package com.xywg.iot.modular.remote.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfig;
import com.xywg.iot.modular.remote.model.vo.DeviceRemoteConfigVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author z
 * @since 2019-01-17
 */
public interface DeviceRemoteConfigService extends IService<DeviceRemoteConfig> {

    /**
     * 查询批量配置履历
     * @param dto
     * @return
     */
    ResultPage<DeviceRemoteConfigVO> selectConfigPageList(QueryWhereDto<DeviceRemoteConfig> dto);
}
