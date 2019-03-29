package com.xywg.test.ceshi.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:34 2019/2/20
 * Modified By : wangyifei
 */
public class Const{
    /**
     *   线程池
     */
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10000,10000,200,TimeUnit.MILLISECONDS,new ArrayBlockingQueue(10000)) ;


    public static final String clientId = "client";

    public static AtomicInteger count =  new AtomicInteger();

    /**
     * 绑定通道
     */
    public static final AttributeKey<XySession> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");


    /**
     * 客户端通道
     * key = clientId
     */
    public static Map<String, Channel> clientChannels = new ConcurrentHashMap<>();


    public static void send(Channel channel,String data){
        ByteBuf byteBuf= Unpooled.buffer(data.length());
        byteBuf.writeBytes(data.getBytes());
        channel.writeAndFlush(byteBuf);
    }
    public static void send (String clientId ,String data){
        ByteBuf byteBuf= Unpooled.buffer(data.length());
        byteBuf.writeBytes(data.getBytes());
        clientChannels.get(clientId).writeAndFlush(byteBuf);
    }


}
