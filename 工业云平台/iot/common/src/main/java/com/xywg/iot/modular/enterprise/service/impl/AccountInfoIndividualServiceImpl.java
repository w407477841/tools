package com.xywg.iot.modular.enterprise.service.impl;


import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.modular.enterprise.dao.AccountInfoIndividualMapper;
import com.xywg.iot.modular.enterprise.model.AccountInfoIndividual;
import com.xywg.iot.modular.enterprise.model.vo.AccountInfoIndividualVo;
import com.xywg.iot.modular.enterprise.service.AccountInfoIndividualService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 个人用户基本信息 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@Service
public class AccountInfoIndividualServiceImpl extends ServiceImpl<AccountInfoIndividualMapper, AccountInfoIndividual> implements AccountInfoIndividualService {

    @Override
    public AccountInfoIndividualVo getAccountInfoIndividual(Wrapper<AccountInfoIndividualVo> wrapper) {
        return baseMapper.getAccountInfoIndividual(wrapper);
    }
}
