package com.wyf.rabbitmqtest.fanoutpublish.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:46 2019/4/29
 * Modified By : wangyifei
 */
@Slf4j
public class QueueConfig {
    @Bean
 public   Queue emailQueue(){
        return new Queue("zhgd.device.email");
    }
    @Bean
 public   Queue phoneQueue(){
        return new Queue("zhgd.device.phone");

    }


}
