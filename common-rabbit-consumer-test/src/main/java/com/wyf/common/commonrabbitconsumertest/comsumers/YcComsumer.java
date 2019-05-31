package com.wyf.common.commonrabbitconsumertest.comsumers;

import com.rabbitmq.client.Channel;
import com.wyf.common.commonrabbitconsumer.consumer.BaseReceiver;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:22 2019/5/8
 * Modified By : wangyifei
 */
@Component
@RabbitListener(queues={"wyf.yc"})
@Slf4j
public class YcComsumer extends BaseReceiver {


    @Override
    public void success(String hello, Channel channel, Message message) {

            log.info(hello);

    }
}
