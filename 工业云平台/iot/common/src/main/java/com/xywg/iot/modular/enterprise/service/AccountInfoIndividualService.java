package com.xywg.iot.modular.enterprise.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.iot.modular.enterprise.model.AccountInfoIndividual;
import com.xywg.iot.modular.enterprise.model.vo.AccountInfoIndividualVo;

/**
 * <p>
 * 个人用户基本信息 服务类
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
public interface AccountInfoIndividualService extends IService<AccountInfoIndividual> {

    AccountInfoIndividualVo getAccountInfoIndividual(Wrapper<AccountInfoIndividualVo> wrapper);
}
