package com.xywg.iot.modular.cloud.service;

import com.xywg.iot.base.ICacheService;
import com.xywg.iot.modular.cloud.model.CloudUser;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyf
 * @since 2018-12-19
 */
public interface ICloudUserService extends IService<CloudUser>  ,ICacheService <CloudUser>{
}
