package com.xywg.iot.modular.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.xycache.annotations.OpenCache;
import com.xywg.iot.xycache.annotations.RemoveCache;
import com.xywg.iot.modular.system.model.SysRole;
import com.xywg.iot.modular.system.model.SysUser;
import com.xywg.iot.modular.system.dao.SysUserMapper;
import com.xywg.iot.modular.system.service.ISysRoleService;
import com.xywg.iot.modular.system.service.ISysUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modular.system.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.Oneway;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2019-01-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
@Autowired
    private ISysRoleService  roleService;

    @Override
    @OpenCache(exp = 500 , model = "sysuser",clazz = UserVO.class)
    public UserVO selectCacheOne(String key) {
        Wrapper<SysUser> wrapper  =new EntityWrapper();
        wrapper.eq("username",key);
         SysUser sysUser =  this.selectOne(wrapper);
         UserVO userVO = new UserVO();
        BeanUtils.copyProperties(sysUser,userVO);

        Wrapper<SysRole> roleWrapper  =new EntityWrapper();
        roleWrapper.where("id  in (select role_id from sys_user_role where user_id = {0})",sysUser.getId());
        List<SysRole> roles =  roleService.selectList(roleWrapper);
        userVO.setRoles(roles);
        return userVO;
    }

    @Override
    @RemoveCache(model = "sysuser")
    public void removeCaches(String key) {

    }
}
