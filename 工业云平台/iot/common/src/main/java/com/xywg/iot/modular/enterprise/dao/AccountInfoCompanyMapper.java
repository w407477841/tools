package com.xywg.iot.modular.enterprise.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompany;
import com.xywg.iot.modular.enterprise.model.vo.AccountInfoCompanyVo;
import com.xywg.iot.modular.enterprise.model.vo.AuditVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公司基本信息 Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
public interface AccountInfoCompanyMapper extends BaseMapper<AccountInfoCompany> {

    AccountInfoCompanyVo getAccountInfoCompany(Integer individualId);

    /**
     * 获取待审核认证列表
     * @param page
     * @param map
     * @return
     * @throws Exception
     */
    List<AuditVo> getAuditList(Page<AuditVo> page,Map<String, Object> map)throws Exception;
}
