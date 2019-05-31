package com.wyf.rabbitmqtest.rabbitadmin.service.impl;

import com.wyf.rabbitmqtest.rabbitadmin.model.MqBinding;
import com.wyf.rabbitmqtest.rabbitadmin.mapper.MqBindingMapper;
import com.wyf.rabbitmqtest.rabbitadmin.service.IMqBindingService;
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
public class MqBindingServiceImpl extends ServiceImpl<MqBindingMapper, MqBinding> implements IMqBindingService {

}
