package com.xywg.iot.modular.colud.controller.device;


import com.xywg.iot.base.BasalController;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.device.model.DeviceErrorLog;
import com.xywg.iot.modular.device.service.IDeviceErrorLogService;
import com.xywg.log.annotations.OpenLog;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author z
 * @since 2019-03-01
 */
@RestController
@RequestMapping("/business/deviceLog")
public class DeviceErrorLogController extends BasalController<DeviceErrorLog,IDeviceErrorLogService> {

    /**
     * 查询列表
     * @param dto
     * @return
     */
    @PostMapping("selectErrorLogPageList")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultPage<DeviceErrorLog> selectErrorLogPageList(@RequestBody QueryWhereDto<DeviceErrorLog> dto) {
        return service.selectErrorLogPageList(dto);
    }


}

