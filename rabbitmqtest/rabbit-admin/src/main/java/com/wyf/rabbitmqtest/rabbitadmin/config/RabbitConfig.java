package com.wyf.rabbitmqtest.rabbitadmin.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wyf.rabbitmqtest.rabbitadmin.callback.ConfirmCallbackHandler;
import com.wyf.rabbitmqtest.rabbitadmin.callback.ErrorCallbackHandler;
import com.wyf.rabbitmqtest.rabbitadmin.model.MqBinding;
import com.wyf.rabbitmqtest.rabbitadmin.model.MqExchange;
import com.wyf.rabbitmqtest.rabbitadmin.model.MqQueue;
import com.wyf.rabbitmqtest.rabbitadmin.service.IMqBindingService;
import com.wyf.rabbitmqtest.rabbitadmin.service.IMqExchangeService;
import com.wyf.rabbitmqtest.rabbitadmin.service.IMqQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 23:27 2019/5/2
 * Modified By : wangyifei
 */
@Configuration
@Import(value =  {ConfirmCallbackHandler.class,ErrorCallbackHandler.class})
@Slf4j
public class RabbitConfig {


    private final ConnectionFactory connectionFactory;
    private final ConfirmCallbackHandler confirmCallbackHandler;
    private final  ErrorCallbackHandler errorCallbackHandler;
    private final IMqQueueService mqQueueService;
    private final IMqBindingService mbqBindingService;
    private final IMqExchangeService mqExchangeService;
    @Autowired
    public RabbitConfig(ConnectionFactory connectionFactory, ConfirmCallbackHandler confirmCallbackHandler, ErrorCallbackHandler errorCallbackHandler, IMqQueueService mqQueueService, IMqBindingService mbqBindingService, IMqExchangeService mqExchangeService) {
        this.connectionFactory = connectionFactory;
        this.confirmCallbackHandler = confirmCallbackHandler;
        this.errorCallbackHandler = errorCallbackHandler;
        this.mqQueueService = mqQueueService;
        this.mbqBindingService = mbqBindingService;
        this.mqExchangeService = mqExchangeService;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(){
        RabbitAdmin rabbitAdmin =  new RabbitAdmin(connectionFactory);
        List<MqExchange> exchanges =  mqExchangeService.selectList(null);
        log.info("准备初始化 交换机 共{"+exchanges.size()+"}个");
        exchanges.forEach(exchange->{
            switch (exchange.getType()){
                case "fanout" :
                    rabbitAdmin.declareExchange( new FanoutExchange(exchange.getName()));
                    break;
                case "topic" :
                    rabbitAdmin.declareExchange( new TopicExchange(exchange.getName()));
                    break;
                case "headers" :
                    rabbitAdmin.declareExchange( new HeadersExchange(exchange.getName()));
                    break;
                default:
                    rabbitAdmin.declareExchange( new DirectExchange(exchange.getName()));
                    break;
            }
        });

        List<MqQueue> queues =  mqQueueService.selectList(null);
        log.info("准备初始化 队列 共{"+queues.size()+"}个");
        queues.forEach(queue->{
            rabbitAdmin.declareQueue(new Queue(queue.getName()));
        });


        Wrapper<MqBinding> bindingWrapper = new EntityWrapper<>();
        bindingWrapper.setSqlSelect("queue as queueName","exchange as exchangeName","(select type from t_mq_exchange where t_mq_exchange.name = t_mq_binding.exchange) as type"   ,"routing");
        List<Map<String,Object>> bindings = mbqBindingService.selectMaps(bindingWrapper);
        log.info("准备绑定订阅路由 共{"+bindings.size()+"}个");
        bindings.forEach(binding->{
            switch ((String)binding.get("type")){
                case "fanout" :
                    rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue((String)binding.get("queueName"))).to(new FanoutExchange((String)binding.get("exchangeName"))));
                    break;
                case "topic" :
                    rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue((String)binding.get("queueName"))).to(new TopicExchange((String)binding.get("exchangeName"))).with((String)binding.get("routing")));
                    break;
                case "headers" :
                    rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue((String)binding.get("queueName"))).to(new HeadersExchange((String)binding.get("exchangeName"))).where("").exists());
                    break;
                default:
                    rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue((String)binding.get("queueName"))).to(new DirectExchange((String)binding.get("exchangeName"))).with((String)binding.get("routing")));
                    break;
            }


        });

        return rabbitAdmin;

    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        rabbitTemplate.setReturnCallback(errorCallbackHandler);
        rabbitTemplate.setConfirmCallback(confirmCallbackHandler);
        return rabbitTemplate;
    }


}
