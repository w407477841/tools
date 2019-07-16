package com.xywg.iot.modular.colud.service;

import com.xywg.iot.modular.enterprise.model.vo.AuditVo;

/***
 *@author:jixiaojun
 *DATE:2019/1/25
 *TIME:11:21
 */
public interface UserRoleService {
    /**
     * 修改角色
     * @param auditVo
     * @return
     * @throws Exception
     */
    boolean updateRole(AuditVo auditVo) throws Exception;
}
