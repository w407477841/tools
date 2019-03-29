package com.xywg.tools.rpc.netty.config;

import com.xywg.tools.rpc.netty.RpcChannelInit;
import com.xywg.tools.rpc.netty.properties.RpcProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:47 2019/2/22
 * Modified By : wangyifei
 */
@Configuration
public class RpcConfig {
    @Autowired
    private RpcProperties properties;
    @Autowired
    private RpcChannelInit channelInit  ;
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConfig.class);

    public void start() throws InterruptedException {

        LOGGER.info("启动");

        Integer port = properties.getPost();

        // 配置服务器端的NIO线程组
        EventLoopGroup bossGroup  =new NioEventLoopGroup();
        EventLoopGroup workGroup  =new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap  =  new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(channelInit);
            //绑定端口，同步等待成功
            ChannelFuture channelFuture  =bootstrap.bind(port).sync();
            LOGGER.info("#####################");
            LOGGER.info("######端口:["+port+"]#######");
            LOGGER.info("#####################");
            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            LOGGER.info("#####################");
            LOGGER.info("######优雅退出#######");
            LOGGER.info("#####################");
        }

    }


}
