package com.wyf.rabbitmqtest.fanoutpublish.config;

import com.wyf.rabbitmqtest.fanoutpublish.callback.ConfirmCallbackHandler;
import com.wyf.rabbitmqtest.fanoutpublish.callback.ErrorCallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:33 2019/4/29
 * Modified By : wangyifei
 */
@Configuration
@Import(value={ExchangeConfig.class,QueueConfig.class,ConfirmCallbackHandler.class,ErrorCallbackHandler.class})
@Slf4j
public class RabbitmqConfig {

    private ExchangeConfig exchangeConfig;
    private QueueConfig queueConfig;
    private ConfirmCallbackHandler confirmCallbackHandler;
    private ErrorCallbackHandler errorCallbackHandler;
    private ConnectionFactory connectionFactory;

    public RabbitmqConfig(ExchangeConfig exchangeConfig, QueueConfig queueConfig, ConfirmCallbackHandler confirmCallbackHandler, ErrorCallbackHandler errorCallbackHandler, ConnectionFactory connectionFactory) {
        this.exchangeConfig = exchangeConfig;
        this.queueConfig = queueConfig;
        this.confirmCallbackHandler = confirmCallbackHandler;
        this.errorCallbackHandler = errorCallbackHandler;
        this.connectionFactory = connectionFactory;
    }

    /**
     *  交换机
     */
    public static final String FANOUT_EXCHANGE_KEY = "wyf.test.fanout";
    /**
     * 交换机
     */
    public static final String TOPIC_EXCHANGE_KEY  = "wyf.test.topic";

    public static final String ROUTING_KEY = "zhgd.device.*";


   // @Bean
    public Binding queue2fanout(){
    return BindingBuilder.bind(queueConfig.emailQueue()).to(exchangeConfig.fanoutExchange());
    }
  //  @Bean
    public Binding queue2topic(){
        return BindingBuilder.bind(queueConfig.emailQueue()).to(exchangeConfig.topicExchange()).with(ROUTING_KEY);
    }

        /**
       * 绑定exchange & queue & routekey.
       *
       * @param queueMessage 队列
       * @param exchange   交换机
       * @param routekey   路由
       * @return
       */
    public Binding bindingExchange(Queue queueMessage, TopicExchange exchange, String routekey) {
    return BindingBuilder.bind(queueMessage).to(exchange).with(routekey);
    }



    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConfirmCallback(confirmCallbackHandler);
        rabbitTemplate.setReturnCallback(errorCallbackHandler);
        return rabbitTemplate;
    }


}
