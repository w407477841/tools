package com.wyf.rabbitmqtest.rabbitadminconsumer.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wyf.rabbitmqtest.rabbitadminconsumer.mapper.MqBindingMapper;
import com.wyf.rabbitmqtest.rabbitadminconsumer.model.MqBinding;
import com.wyf.rabbitmqtest.rabbitadminconsumer.service.IMqBindingService;
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
public class MqBindingServiceImpl extends ServiceImpl<MqBindingMapper, MqBinding> implements IMqBindingService {

}
