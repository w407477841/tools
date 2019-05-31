package com.wyf.rabbitmqtest.fanoutpublish.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:34 2019/4/29
 * Modified By : wangyifei
 */
@Slf4j
public class ExchangeConfig {

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(RabbitmqConfig.FANOUT_EXCHANGE_KEY);
    }
    @Bean
    public TopicExchange topicExchange(){
        log.info("topicExchange 初始化");
        return new TopicExchange(RabbitmqConfig.TOPIC_EXCHANGE_KEY);
    }


}
