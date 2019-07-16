package com.xywg.iot.modular.colud.netty.config;

import com.xywg.iot.modular.colud.netty.handler.IotHubHandler;
import com.xywg.iot.util.NettyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:28 2018/12/11
 * Modified By : wangyifei
 */
@Configuration
public class NettyConfig {
    @Value("${netty.iotNettyUrl}")
    private String iotNettyUrl;
    @Value("${netty.iotNettyPort}")
    private String iotNettyPort;
    @Autowired
    private NettyUtil nettyUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyConfig.class);

    public void start(String loginJson,String clientId) throws InterruptedException {


        // 配置服务器端的NIO线程组
        EventLoopGroup bossGroup  =  new NioEventLoopGroup();

        try {

            Bootstrap bootstrap =new Bootstrap();
            bootstrap.group(bossGroup)
                    .remoteAddress(iotNettyUrl,Integer.parseInt(iotNettyPort))
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IotHubHandler(loginJson,clientId,nettyUtil));
                        }
                    });
            //连接服务端，同步等待成功
            ChannelFuture channelFuture  =bootstrap.connect().sync();
            LOGGER.info("#####################");
            LOGGER.info("######连接成功 成功#######");
            LOGGER.info("#####################");
            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            LOGGER.info("#####################");
            LOGGER.info("######客户端关闭#######");
            LOGGER.info("#####################");
        }

    }


}
