package com.xywg.iot.modular.colud.controller.device;


import com.xywg.iot.base.BasalController;
import com.xywg.iot.modular.device.model.DeviceConfigFileRecord;
import com.xywg.iot.modular.device.service.DeviceConfigFileRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjy
 * @since 2019-01-04
 */
@RestController
@RequestMapping("/business/configFileRecord")
public class DeviceConfigFileRecordController extends BasalController<DeviceConfigFileRecord,DeviceConfigFileRecordService> {

}

