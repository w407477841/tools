package com.xywg.iot.modular.colud.controller.upgrade;


import com.xywg.iot.base.BasalController;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecord;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradeRecordService;
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
@RequestMapping("/business/upgradeRecord")
public class DeviceUpgradeRecordController extends BasalController<DeviceUpgradeRecord,IDeviceUpgradeRecordService> {

}

