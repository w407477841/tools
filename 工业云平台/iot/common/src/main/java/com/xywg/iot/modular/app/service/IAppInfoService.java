package com.xywg.iot.modular.app.service;

import com.xywg.iot.base.ICacheService;
import com.xywg.iot.modular.app.model.AppInfo;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyifei
 * @since 2019-01-16
 */
public interface IAppInfoService extends IService<AppInfo> ,ICacheService<AppInfo> {

}
