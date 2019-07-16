package com.xywg.iot.modular.enterprise.service;


import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.base.ICacheService;
import com.xywg.iot.dto.EnterpriseCertificationDTO;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompany;
import com.xywg.iot.modular.enterprise.model.vo.AccountInfoCompanyVo;
import com.xywg.iot.vo.ResultVO;

/**
 * <p>
 * 公司基本信息 服务类
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
public interface AccountInfoCompanyService extends IService<AccountInfoCompany>,ICacheService<AccountInfoCompany> {

    /**
     * 企业认证信息插入
     * @param enterpriseCertificationDTO
     * @return
     */
    ResultVO insertEnterpriseCertification(EnterpriseCertificationDTO enterpriseCertificationDTO);

    /**
     * 根据登录者获取企业认证信息获取
     * @param userId
     * @return
     */
    ResultVO getEnterpriseCertification(String userId);


    /**
     * 修改填写的企业认证信息
     * @param enterpriseCertificationDTO
     * @return
     */
    ResultVO updateEnterpriseCertification(EnterpriseCertificationDTO enterpriseCertificationDTO);


    AccountInfoCompanyVo getAccountInfoCompany(Integer individualId);

}
