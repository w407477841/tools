package com.wyf.rabbitmqtest.rabbitadmin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.wyf.rabbitmqtest.rabbitadmin.model.MqBinding;
import com.wyf.rabbitmqtest.rabbitadmin.model.MqExchange;
import com.wyf.rabbitmqtest.rabbitadmin.model.MqQueue;
import com.wyf.rabbitmqtest.rabbitadmin.service.IMqBindingService;
import com.wyf.rabbitmqtest.rabbitadmin.service.IMqExchangeService;
import com.wyf.rabbitmqtest.rabbitadmin.service.IMqQueueService;
import com.wyf.rabbitmqtest.rabbitadmin.utils.SpingUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootApplication

public class RabbitAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitAdminApplication.class, args);


    }

}
