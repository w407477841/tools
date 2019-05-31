package com.wyf.rabbitmqtest.rabbitadmin.service.impl;

import com.wyf.rabbitmqtest.rabbitadmin.model.MqQueue;
import com.wyf.rabbitmqtest.rabbitadmin.mapper.MqQueueMapper;
import com.wyf.rabbitmqtest.rabbitadmin.service.IMqQueueService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
