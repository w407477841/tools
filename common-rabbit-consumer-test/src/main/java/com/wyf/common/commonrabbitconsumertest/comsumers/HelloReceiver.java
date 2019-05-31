package com.wyf.common.commonrabbitconsumertest.comsumers;

import com.rabbitmq.client.Channel;
import com.wyf.common.commonrabbitconsumer.consumer.BaseReceiver;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 18:40 2019/4/28
 * Modified By : wangyifei
 */
@Component
@RabbitListener(queues = {"wyf.email","wyf.message"})
public class HelloReceiver extends BaseReceiver {

    @Override
    public void success(String hello, Channel channel, Message message) {
        if(message.getMessageProperties().getDeliveryTag()==1||message.getMessageProperties().getDeliveryTag()==3){
            //  模拟异常
            throw new RuntimeException("我就是要报错");
        }
        System.out.println(message.toString());
    }
}
