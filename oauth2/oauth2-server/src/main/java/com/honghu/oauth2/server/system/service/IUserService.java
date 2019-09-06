package com.honghu.oauth2.server.system.service;

import com.honghu.oauth2.system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.honghu.oauth2.vo.ResultVO;
import com.honghu.oauth2.vo.user.ModifyPasswordV1;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyf
 * @since 2019-08-28
 */
public interface IUserService extends IService<User> {

     ResultVO modifyPasswordV1(ModifyPasswordV1 modifyPasswordV1);

}
