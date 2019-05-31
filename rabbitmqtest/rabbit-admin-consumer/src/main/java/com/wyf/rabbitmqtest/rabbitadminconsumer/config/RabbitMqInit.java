package com.wyf.rabbitmqtest.rabbitadminconsumer.config;

import com.rabbitmq.client.Channel;
import com.wyf.rabbitmqtest.rabbitadminconsumer.model.MqExchange;
import com.wyf.rabbitmqtest.rabbitadminconsumer.model.MqQueue;
import com.wyf.rabbitmqtest.rabbitadminconsumer.service.IMqExchangeService;
import com.wyf.rabbitmqtest.rabbitadminconsumer.service.IMqQueueService;
import com.wyf.rabbitmqtest.rabbitadminconsumer.utils.SpingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:28 2019/5/5
 * Modified By : wangyifei
 */
public class RabbitMqInit {


    public void init (){

        System.out.println("准备初始化 - 消费者");
        ConnectionFactory connectionFactory = SpingUtil.applicationContext.getBean(ConnectionFactory.class);
        Connection connection = connectionFactory.createConnection();

        Channel channel = connection.createChannel(false);
        IMqExchangeService mqExchangeService =  SpingUtil.applicationContext.getBean(IMqExchangeService.class);
        List<MqExchange> exchanges =  mqExchangeService.selectList(null);
        System.out.println("准备初始化 交换机 共{"+exchanges.size()+"}个");
        exchanges.forEach(exchange->{
            try {
                channel.exchangeDeclare(exchange.getName(),exchange.getType(),true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        IMqQueueService mqQueueService =  SpingUtil.applicationContext.getBean(IMqQueueService.class);
        List<MqQueue> queues =  mqQueueService.selectList(null);
        System.out.println("准备初始化 队列 共{"+queues.size()+"}个");
        queues.forEach(queue->{
            try {
                channel.queueDeclare(queue.getName(),true,false,false,null);
         //       channel.basicConsume(queue.getName(),false,new MyConsumer(channel,queue.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }


}
