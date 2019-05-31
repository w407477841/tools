package com.wyf.common.commonrabbitconsumer.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wyf.common.commonrabbitconsumer.model.MqError;
import com.wyf.common.commonrabbitconsumer.service.IMqErrorService;
import com.wyf.common.commonrabbitconsumer.service.impl.MqErrorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 23:27 2019/5/2
 * Modified By : wangyifei
 */
@Configuration
@ConditionalOnProperty(value = "enabled" , prefix = "spring.rabbitmq" ,matchIfMissing = true )
public class RabbitConfig {


    private final ConnectionFactory connectionFactory;
    @Autowired
    @SuppressWarnings("all")
    public RabbitConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    @Bean
    public RabbitAdmin rabbitAdmin(){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public IMqErrorService mqErrorService(){

        return new MqErrorServiceImpl();
    }


    @RestController
    @RequestMapping(value="/api/mq")
    public class ErrorApi {

        private final IMqErrorService mqErrorService;
        @Autowired
        public ErrorApi(IMqErrorService mqErrorService) {
            this.mqErrorService = mqErrorService;
        }

        @GetMapping("errors")
        public Object errors(String exchange,String queue,String routing,String description){

            Wrapper<MqError> wrapper = new EntityWrapper<>();
            wrapper.like(StrUtil.isNotBlank(exchange),"exchange",exchange);
            wrapper.like(StrUtil.isNotBlank(queue),"queue",queue);
            wrapper.like(StrUtil.isNotBlank(routing),"routing",routing);
            wrapper.like(StrUtil.isNotBlank(description),"description",description);
            return mqErrorService.selectList(wrapper);
        }

    }

}
