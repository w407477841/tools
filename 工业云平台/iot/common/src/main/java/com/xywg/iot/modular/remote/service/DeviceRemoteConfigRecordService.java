package com.xywg.iot.modular.remote.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.vo.DeviceConfigVo;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfigRecord;
import com.xywg.iot.modular.remote.model.vo.DeviceRemoteConfigRecordVO;
import com.xywg.iot.vo.ResultVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjy
 * @since 2019-01-15
 */
public interface DeviceRemoteConfigRecordService extends IService<DeviceRemoteConfigRecord> {

    /**
     * 获取设备列表
     * @param dto
     * @return
     */
    ResultPage<DeviceInfoVo> selectDevicePageList(QueryWhereDto<DeviceInfo> dto);

    /**
     * 不带分页的设备列表
     * @param dto
     * @return
     */
    ResultVO selectDeviceList(QueryWhereDto<DeviceInfo> dto);

    /**
     * 获取单个设备信息
     * @param dto
     * @return
     */
    ResultVO getDeviceInfoById(QueryWhereDto<DeviceInfo> dto);
    /**
     * 设备远程配置
     * @param dto
     * @return
     */
    ResultVO batchUpdateDeviceConfig(DeviceConfigVo dto);

    /**
     * 带分页的单个设备配置履历
     * @param dto
     * @return
     */
    ResultPage<DeviceRemoteConfigRecordVO> getDeviceConfigHistoryPage( QueryWhereDto<DeviceRemoteConfigRecord> dto);

    /**
     * 查询配置履历
     * @param dto
     * @return
     */
    ResultPage<DeviceRemoteConfigRecordVO> getDeviceRemoteConfigRecordList(QueryWhereDto<DeviceRemoteConfigRecord> dto);


    /**
     *  批量恢复远程设置
     * @param dto
     * @return
     */
    ResultVO batchRecoverDeviceConfig(QueryWhereDto dto);


    /**
     * 获取设备配置履历
     * @param dto
     * @return
     */
    List<DeviceRemoteConfigRecordVO> getDeviceConfigHistory(QueryWhereDto dto);

}
