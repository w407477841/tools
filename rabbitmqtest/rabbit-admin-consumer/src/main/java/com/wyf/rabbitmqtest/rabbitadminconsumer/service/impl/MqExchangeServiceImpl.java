package com.wyf.rabbitmqtest.rabbitadminconsumer.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;


import com.wyf.rabbitmqtest.rabbitadminconsumer.mapper.MqExchangeMapper;
import com.wyf.rabbitmqtest.rabbitadminconsumer.model.MqExchange;
import com.wyf.rabbitmqtest.rabbitadminconsumer.service.IMqExchangeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2019-05-03
 */
@Service
public class MqExchangeServiceImpl extends ServiceImpl<MqExchangeMapper, MqExchange> implements IMqExchangeService {

}
