package com.wyf.rabbitmqtest.receive;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.wyf.rabbitmqtest.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 18:40 2019/4/28
 * Modified By : wangyifei
 */
@Component

public class HelloReceiver {
    @RabbitListener(bindings = {@QueueBinding(value=@Queue("queue.a"),exchange = @Exchange(type="fanout",value="com.wyf.fanout"))})
    @RabbitHandler
   public void handler1(String hello, Channel channel, Message message)throws  Exception{
        System.out.println("handler1");
       System.out.println("HelloReceiver收到  : " + hello +"收到时间"+new Date());
       try {
           //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
           if(message.getMessageProperties().getDeliveryTag()==1|message.getMessageProperties().getDeliveryTag()==2|message.getMessageProperties().getDeliveryTag()==3){
               throw new Exception();
           }
           channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
           System.out.println("receiver success");
       } catch (Exception e) {
           e.printStackTrace();
           if (message.getMessageProperties().getRedelivered())
           {
               System.out.println("消息已重复处理失败,拒绝再次接收...");
               // 拒绝消息
               channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
           }
           else
           {
               System.out.println("消息即将再次返回队列处理...");
               // requeue为是否重新回到队列
               channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
           }
       }

   }
    @RabbitListener(bindings = {@QueueBinding(value=@Queue("queue.b"),exchange = @Exchange(type="fanout",value="com.wyf.fanout"))})
    @RabbitHandler
    public void handler2(String hello, Channel channel, Message message){
        System.out.println("handler2");
        System.out.println("HelloReceiver收到  : " + hello +"收到时间"+new Date());
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println("receiver success");
        } catch (Exception e) {
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.out.println("receiver fail");
        }

    }


    @RabbitListener(bindings = {@QueueBinding(value=@Queue("queue.#"),exchange = @Exchange(type="topic",value="com.wyf.topic"))})
    @RabbitHandler
    public void handler3(String hello, Channel channel, Message message) {
        System.out.println("queue.# handler");
        System.out.println("HelloReceiver收到  : " + hello + "收到时间" + new Date());
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("receiver success");
        } catch (Exception e) {
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.out.println("receiver fail");
        }
    }

    @RabbitListener(bindings = {@QueueBinding(value=@Queue("queue.*"),exchange = @Exchange(type="topic",value="com.wyf.topic"))})
    @RabbitHandler
    public void handler4(String hello, Channel channel, Message message) {
        System.out.println("queue.* handler");
        System.out.println("HelloReceiver收到  : " + hello + "收到时间" + new Date());
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("receiver success");
        } catch (Exception e) {
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.out.println("receiver fail");
        }
    }

}
