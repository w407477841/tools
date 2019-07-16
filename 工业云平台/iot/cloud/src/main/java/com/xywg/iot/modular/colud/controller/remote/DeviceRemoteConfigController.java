package com.xywg.iot.modular.colud.controller.remote;


import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.base.BasalController;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfig;
import com.xywg.iot.modular.remote.model.vo.DeviceRemoteConfigVO;
import com.xywg.iot.modular.remote.service.DeviceRemoteConfigService;
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
 * @since 2019-01-17
 */
@RestController
@RequestMapping("/business/remoteConfig")
public class DeviceRemoteConfigController extends BasalController<DeviceRemoteConfig,DeviceRemoteConfigService> {

    /**
     * 查询批量配置履历
     * @param dto
     * @return
     */
    @PostMapping("selectConfigPageList")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD})
    public ResultPage<DeviceRemoteConfigVO> selectConfigPageList(@RequestBody QueryWhereDto<DeviceRemoteConfig> dto) {
        return service.selectConfigPageList(dto);
    }
}

