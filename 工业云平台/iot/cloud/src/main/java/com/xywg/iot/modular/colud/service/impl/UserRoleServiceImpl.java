package com.xywg.iot.modular.colud.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.modular.colud.service.UserRoleService;
import com.xywg.iot.modular.enterprise.dao.AccountInfoCompanyMapper;
import com.xywg.iot.modular.enterprise.dao.AccountInfoIndividualMapper;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompany;
import com.xywg.iot.modular.enterprise.model.AccountInfoIndividual;
import com.xywg.iot.modular.enterprise.model.vo.AuditVo;
import com.xywg.iot.modular.system.dao.SysRoleMapper;
import com.xywg.iot.modular.system.dao.SysUserMapper;
import com.xywg.iot.modular.system.dao.SysUserRoleMapper;
import com.xywg.iot.modular.system.model.SysRole;
import com.xywg.iot.modular.system.model.SysUser;
import com.xywg.iot.modular.system.model.SysUserRole;
import com.xywg.iot.modular.system.service.ISysUserService;
import com.xywg.iot.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/25
 *TIME:11:21
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private AccountInfoIndividualMapper accountInfoIndividualMapper;
    @Autowired
    private AccountInfoCompanyMapper accountInfoCompanyMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysUserService sysUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(AuditVo auditVo) throws Exception {
        boolean isSuccess;
        AccountInfoIndividual accountInfoIndividual = new AccountInfoIndividual();
        AccountInfoCompany accountInfoCompany = new AccountInfoCompany();
        BeanUtil.copyProperties(auditVo,accountInfoIndividual);
        BeanUtil.copyProperties(auditVo,accountInfoCompany);
        accountInfoCompany.setId(auditVo.getId());
        accountInfoIndividual.setId(auditVo.getIndividualId());
        if(accountInfoIndividualMapper.updateById(accountInfoIndividual) > 0) {
            accountInfoCompanyMapper.updateById(accountInfoCompany);
            isSuccess = true;
        }else {
            isSuccess = false;
        }
        if(new Integer(3).equals(auditVo.getAuditStatus())) {
            return isSuccess;
        }
        Wrapper<SysRole> wrapper = new EntityWrapper<>();
        wrapper.eq("name", "ROLE_"+RoleConstant.AUTH);
        List<SysRole> list = sysRoleMapper.selectList(wrapper);
        Wrapper<SysUserRole> roleWrapper = new EntityWrapper<>();
        roleWrapper.eq("user_id",accountInfoIndividual.getUserId());
        sysUserRoleMapper.delete(roleWrapper);
        SysUser sysUser = sysUserMapper.selectById(accountInfoIndividual.getUserId());
        sysUserService.removeCaches(sysUser.getUsername());
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(list.get(0).getId());
        sysUserRole.setDelFlag(0);
        sysUserRole.setId(new Long(SnowFlakeUtil.getFlowIdInstance().nextId()).toString());
        sysUserRole.setUserId(accountInfoIndividual.getUserId());
        sysUserRoleMapper.insertUserRole(sysUserRole);
        return isSuccess;
    }
}
