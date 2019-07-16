package com.xywg.iot.modular.colud.controller.device;

import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.modular.device.model.DeviceConfigFile;
import com.xywg.iot.modular.device.service.IDeviceConfigFileService;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.log.annotations.OpenLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:59 2019/3/27
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("/business/deviceConfigFile")
@Slf4j
public class DeviceConfigFileController extends BaseControllerPlus<DeviceConfigFile,IDeviceConfigFileService> {


    @PostMapping("selectDevicePageList")
    @OpenLog
    public ResultPage<DeviceInfoVo> selectDevicePageList(@RequestBody QueryWhereDto<DeviceConfigFile> dto) {
        return service.selectDevicePageList(dto);
    }

}
