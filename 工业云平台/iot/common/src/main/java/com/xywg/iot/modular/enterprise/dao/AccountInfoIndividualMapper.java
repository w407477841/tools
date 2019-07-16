package com.xywg.iot.modular.enterprise.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.modular.enterprise.model.AccountInfoIndividual;
import com.xywg.iot.modular.enterprise.model.vo.AccountInfoIndividualVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 个人用户基本信息 Mapper 接口
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
public interface AccountInfoIndividualMapper extends BaseMapper<AccountInfoIndividual> {
    /**
     * 获取详细信息
     * @param wrapper
     * @return
     */
    AccountInfoIndividualVo getAccountInfoIndividual(@Param("ew") Wrapper<AccountInfoIndividualVo> wrapper);
}
