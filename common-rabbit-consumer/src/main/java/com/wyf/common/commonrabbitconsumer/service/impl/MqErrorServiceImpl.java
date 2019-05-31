package com.wyf.common.commonrabbitconsumer.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wyf.common.commonrabbitconsumer.mapper.MqErrorMapper;
import com.wyf.common.commonrabbitconsumer.model.MqError;
import com.wyf.common.commonrabbitconsumer.service.IMqErrorService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2019-05-05
 */
@Service
public class MqErrorServiceImpl extends ServiceImpl<MqErrorMapper, MqError> implements IMqErrorService {

}
