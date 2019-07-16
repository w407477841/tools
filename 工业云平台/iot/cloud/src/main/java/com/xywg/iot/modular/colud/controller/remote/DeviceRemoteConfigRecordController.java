package com.xywg.iot.modular.colud.controller.remote;


import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BasalController;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.vo.DeviceConfigVo;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfigRecord;
import com.xywg.iot.modular.remote.model.vo.DeviceRemoteConfigRecordVO;
import com.xywg.iot.modular.remote.service.DeviceRemoteConfigRecordService;
import com.xywg.iot.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjy
 * @since 2019-01-15
 */
@RestController
@RequestMapping("/business/remote")
public class DeviceRemoteConfigRecordController extends BasalController<DeviceRemoteConfigRecord,DeviceRemoteConfigRecordService>{

    /**
     * 获取带分页的设备列表
     * @param dto
     * @return
     */
    @PostMapping("selectDevicePageList")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultPage<DeviceInfoVo> selectDevicePageList(@RequestBody QueryWhereDto<DeviceInfo> dto) {
        return service.selectDevicePageList(dto);
    }

    /**
     * 获取不带分页的设备列表
     * @param dto
     * @return
     */
    @PostMapping("selectDeviceList")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO selectDeviceList(@RequestBody QueryWhereDto<DeviceInfo> dto) {
        return service.selectDeviceList(dto);
    }

    @PostMapping("getDeviceInfoById")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO getDeviceInfoById(@RequestBody QueryWhereDto<DeviceInfo> dto){
        return service.getDeviceInfoById(dto);
    }

    /**
     * 根据配置id  查询受影响的设备
     * @param dto
     * @return
     */
    @PostMapping("getDeviceRemoteConfigRecordList")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultPage<DeviceRemoteConfigRecordVO> getDeviceRemoteConfigRecordList(@RequestBody QueryWhereDto<DeviceRemoteConfigRecord> dto) {
        return service.getDeviceRemoteConfigRecordList(dto);
    }

    /**
     * 批量远程配置设备
     * @param dto
     * @return
     */
    @PostMapping("batchUpdateDeviceConfig")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO batchUpdateDeviceConfig(@RequestBody DeviceConfigVo dto) {
        return service.batchUpdateDeviceConfig(dto);
    }

    /**
     * 批量恢复远程配置设备
     * @param dto
     * @return
     */
    @PostMapping("batchRecoverDeviceConfig")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultVO batchRecoverDeviceConfig(@RequestBody QueryWhereDto dto) {
        return service.batchRecoverDeviceConfig(dto);
    }


    /**
     * 获取单个设备的配置履历
     * @param dto
     * @return
     */
    @OpenLog
    @PostMapping("getDeviceConfigHistory")
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultPage<DeviceRemoteConfigRecordVO> getDeviceConfigHistory(@RequestBody QueryWhereDto<DeviceRemoteConfigRecord> dto) {
        return service.getDeviceConfigHistoryPage(dto);
    }


}

