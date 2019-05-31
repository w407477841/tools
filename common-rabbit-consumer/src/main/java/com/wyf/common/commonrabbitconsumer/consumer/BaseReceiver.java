package com.wyf.common.commonrabbitconsumer.consumer;


import com.rabbitmq.client.Channel;
import com.wyf.common.commonrabbitconsumer.model.MqError;
import com.wyf.common.commonrabbitconsumer.service.IMqErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:24 2019/5/5
 * Modified By : wangyifei
 */
@Slf4j
public abstract class BaseReceiver {
    @Autowired
    private IMqErrorService iMqErrorService;

    public abstract void success(String hello, Channel channel, Message message);

    @RabbitHandler
    public void handler(String json, Channel channel, Message message) throws IOException {
        try {
            success(json, channel ,message);
            log.info("消息成功消费 | 数据 {}  : 元信息 {}",json,message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered())
            {
             log.info("消息消费失败 | 数据 {}  : 元信息 {} | 异常 {}",json,message,e.getMessage());
                MqError mqError  =new MqError();
                mqError.setError(e.getMessage());
                mqError.setMessage(json);
                mqError.setDeliveryTag(message.getMessageProperties().getDeliveryTag());
                mqError.setTime(new Date());
                mqError.setExchange(message.getMessageProperties().getReceivedExchange());
                mqError.setQueue(message.getMessageProperties().getConsumerQueue());
                mqError.setRoutingKey(message.getMessageProperties().getReceivedRoutingKey());
                iMqErrorService.insert(mqError);
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
            else
            {
                log.info("消息失败重发 | 数据 {}  : 元信息 {} | 异常 {}",json,message,e.getMessage());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }
    }
}
