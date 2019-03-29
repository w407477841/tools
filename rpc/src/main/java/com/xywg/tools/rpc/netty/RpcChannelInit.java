package com.xywg.tools.rpc.netty;

import com.xywg.tools.rpc.netty.properties.RpcProperties;
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
public class RpcChannelInit extends ChannelInitializer<SocketChannel> {


    private RpcHandler rpcHandler;
    private RpcProperties properties ;



    @Autowired
    public RpcChannelInit(RpcHandler rpcHandler, RpcProperties properties) {
        this.properties =  properties;
        this.rpcHandler = rpcHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) {

        ch.pipeline()
                .addLast(new IdleStateHandler(properties.getCycle(), 0, 0, TimeUnit.SECONDS))
                .addLast(rpcHandler);
    }
}
