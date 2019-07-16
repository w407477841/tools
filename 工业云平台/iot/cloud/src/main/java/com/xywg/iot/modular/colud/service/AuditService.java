package com.xywg.iot.modular.colud.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.modular.enterprise.model.vo.AuditVo;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/24
 *TIME:13:40
 */
public interface AuditService {
    /**
     * 获取待审核认证列表
     * @param page
     * @param queryPageDTO
     * @return
     * @throws Exception
     */
    List<AuditVo> getAuditList(Page<AuditVo> page,QueryPageDTO queryPageDTO) throws Exception;
}
