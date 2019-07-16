package com.xywg.iot.modular.system.dao;

import com.xywg.iot.modular.system.model.SysUserRole;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author jxj
 * @since 2019-01-25
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    /**
     * 新增
     * @param sysUserRole
     * @return
     * @throws Exception
     */
    Integer insertUserRole(SysUserRole sysUserRole) throws Exception;
}
