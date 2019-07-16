package com.xywg.iot.modular.colud.controller;

import com.xywg.iot.base.BaseControllerPlus;
import com.xywg.iot.modular.device.model.DevicePropertyRecord;
import com.xywg.iot.modular.device.service.IDevicePropertyRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:15 2019/1/2
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("collect")
public class CollectDetailController extends BaseControllerPlus<DevicePropertyRecord,IDevicePropertyRecordService> {


}
