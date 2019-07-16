package com.xywg.iot.modular.system.service;

import com.xywg.iot.base.ICacheService;
import com.xywg.iot.modular.system.model.SysUser;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.modular.system.vo.UserVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author hjy
 * @since 2019-01-09
 */
public interface ISysUserService extends IService<SysUser> , ICacheService<UserVO> {

}
