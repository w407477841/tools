package com.wyf.rabbitmqtest.rabbitadminconsumer.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wyf.rabbitmqtest.rabbitadminconsumer.mapper.MqQueueMapper;
import com.wyf.rabbitmqtest.rabbitadminconsumer.model.MqQueue;
import com.wyf.rabbitmqtest.rabbitadminconsumer.service.IMqQueueService;
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
public class MqQueueServiceImpl extends ServiceImpl<MqQueueMapper, MqQueue> implements IMqQueueService {

}
