package com.xywg.test.ceshi.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xywg.test.ceshi.common.Const;
import java.util.concurrent.TimeUnit;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:44 2019/3/13
 * Modified By : wangyifei
 */
public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private final String clientId;

    public Client(String clientId) {
        this.clientId = clientId;
    }

    public void start() throws InterruptedException {


        // 配置服务器端的NIO线程组
        EventLoopGroup bossGroup  =  new NioEventLoopGroup();

        try {

            Bootstrap bootstrap =new Bootstrap();
            bootstrap.group(bossGroup)
                    .remoteAddress("192.168.1.64",9060)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(0, 0, 60*30, TimeUnit.SECONDS))
                                    .addLast(new ClientHandler(clientId));
                        }
                    });
            //连接服务端，同步等待成功
            ChannelFuture channelFuture  =bootstrap.connect().sync();
            LOGGER.info("#####################");
            LOGGER.info("######连接成功"+Const.count.incrementAndGet()+"#######");
            LOGGER.info("#####################");
            //等待服务端监听端口关闭

            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            LOGGER.info("#####################");
            LOGGER.info("######客户端关闭"+Const.count.decrementAndGet()+"#######");
            LOGGER.info("#####################");

            //重连

            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new Client(clientId).start();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).start();
        }

    }

}
