package com.wyf.pp.wyfpp.server.impl;

import com.wyf.pp.wyfpp.factory.MarshallingCodeCFactory;
import com.wyf.pp.wyfpp.handler.LoginAuthReqHandler;
import com.wyf.pp.wyfpp.server.BaseServer;
import com.wyf.pp.wyfpp.server.IServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:39 2019/5/28
 * Modified By : wangyifei
 */
@Slf4j
public class Serverimpl extends BaseServer {



    @Override
    public void channel(SocketChannel socketChannel){

        socketChannel.pipeline()
                .addLast(MarshallingCodeCFactory.buildMarshallingDecoder())
                .addLast(MarshallingCodeCFactory.buildMarshallingEncoder())
                .addLast(new LoginAuthReqHandler());


    }

}
