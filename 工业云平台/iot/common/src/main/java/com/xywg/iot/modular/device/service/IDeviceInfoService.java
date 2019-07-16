package com.xywg.iot.modular.device.service;

import com.xywg.iot.base.ICacheService;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.modular.device.vo.DeviceAddrVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jxj
 * @since 2019-01-04
 */
public interface IDeviceInfoService extends IService<DeviceInfo> , ICacheService<DeviceInfo> {

    List<DeviceAddrVO> devceAddrTop5(String user);

}
