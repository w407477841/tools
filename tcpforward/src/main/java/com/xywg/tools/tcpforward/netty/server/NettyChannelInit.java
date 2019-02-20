package com.xywg.tools.tcpforward.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:37 2018/10/15
 * Modified By : wangyifei
 */
@Component
public class NettyChannelInit extends ChannelInitializer<SocketChannel> {


    private AccetperHandler iotHubHandler;




    @Autowired
    public NettyChannelInit(AccetperHandler iotHubHandler) {
        this.iotHubHandler = iotHubHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) {

        ch.pipeline()
                .addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS))
                .addLast(iotHubHandler);
    }
}
