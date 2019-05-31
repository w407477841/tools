package com.wyf.rabbitmqtest.fanoutpublish.controller;

import com.wyf.rabbitmqtest.fanoutpublish.callback.ConfirmCallbackHandler;
import com.wyf.rabbitmqtest.fanoutpublish.callback.ErrorCallbackHandler;
import com.wyf.rabbitmqtest.fanoutpublish.config.ExchangeConfig;
import com.wyf.rabbitmqtest.fanoutpublish.config.QueueConfig;
import com.wyf.rabbitmqtest.fanoutpublish.config.RabbitmqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:26 2019/4/29
 * Modified By : wangyifei
 */
@RestController
@RequestMapping("")
public class PublishController {

    private RabbitTemplate rabbitTemplate;
    private RabbitmqConfig config;
    private ExchangeConfig exchangeConfig;
    private QueueConfig queueConfig;
    @Autowired
    public PublishController(RabbitTemplate rabbitTemplate, RabbitmqConfig config, ExchangeConfig exchangeConfig, QueueConfig queueConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.config = config;
        this.exchangeConfig = exchangeConfig;
        this.queueConfig = queueConfig;
    }

    @GetMapping("publish")
    public Object publish(String routingKey,String data){
        config.bindingExchange(queueConfig.emailQueue(),exchangeConfig.topicExchange(),routingKey);
        rabbitTemplate.convertAndSend(RabbitmqConfig.TOPIC_EXCHANGE_KEY,RabbitmqConfig.ROUTING_KEY,data);
      //  rabbitTemplate.convertAndSend(RabbitmqConfig.TOPIC_EXCHANGE_KEY,routingKey,data);
        return "ok";
    }

}
