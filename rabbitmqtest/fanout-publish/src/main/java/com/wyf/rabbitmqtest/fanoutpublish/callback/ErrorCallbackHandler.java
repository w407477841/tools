package com.wyf.rabbitmqtest.fanoutpublish.callback;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:53 2019/4/29
 * Modified By : wangyifei
 */

@Slf4j
public class ErrorCallbackHandler implements RabbitTemplate.ReturnCallback {


    @Override
    public void returnedMessage(Message message, int flag, String replayText, String exchange, String routingKey) {
        log.info("失败确认");
    }

}
