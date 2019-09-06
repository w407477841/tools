package com.honghu.oauth2.server.system.service.impl;

import com.honghu.oauth2.system.entity.Role;
import com.honghu.oauth2.server.system.mapper.RoleMapper;
import com.honghu.oauth2.server.system.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2019-08-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
