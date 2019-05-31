package com.wyf.rabbitmqtest.rabbitadmin.controller;

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
    @Autowired
    public PublishController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("publish")
    public Object publish(String routingKey,String exchange,String data){
       // rabbitTemplate.convertSendAndReceive("auto.fanout","","wocao ");


       rabbitTemplate.convertSendAndReceive(exchange,routingKey,data);

        return "ok";
    }

}
