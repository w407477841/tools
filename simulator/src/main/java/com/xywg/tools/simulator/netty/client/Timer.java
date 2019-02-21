package com.xywg.tools.simulator.netty.client;

import com.xywg.tools.simulator.netty.common.Const;
import com.xywg.tools.simulator.netty.config.properties.NettyProperties;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:40 2019/2/21
 * Modified By : wangyifei
 */

@Component
public class Timer {

    private final NettyProperties properties;
    @Autowired
    public Timer(NettyProperties properties) {
        this.properties = properties;
    }

    @Scheduled(cron = "0/3 * * * * ?")
    public void send(){
        System.out.println("发送数据 "+properties.getData());
        ByteBuf byteBuf= Unpooled.buffer(properties.getData().length());
        byteBuf.writeBytes(properties.getData().getBytes());
        try {
            Const.clientChannels.get(Const.clientId).writeAndFlush(byteBuf);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("发送异常 ");
        }


    }


}
