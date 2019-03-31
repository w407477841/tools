package com.wyf.iot;

import com.wyf.iot.auto.ServerAutoConfigure;
import com.wyf.iot.common.IpUtils;
import com.wyf.iot.common.RemotingUtil;
import com.wyf.iot.properties.NettyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * netty 服务启动类
 *
 * @author lxr
 * @create 2017-11-18 14:03
 **/
@Setter
@Getter
@lombok.extern.slf4j.Slf4j
public class NettyBootstrapServer implements BootstrapServer {

    private NettyProperties serverBean;

    public NettyProperties getServerBean() {
        return serverBean;
    }
    @Override
    public void setServerBean(NettyProperties serverBean) {
        this.serverBean = serverBean;
    }

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;
    // 启动辅助类
    ServerBootstrap bootstrap=null ;

    /**
     * 服务开启
     */
    @Override
    public void start() {
        initEventPool();
        bootstrap.group(bossGroup, workGroup)
                .channel(useEpoll()?EpollServerSocketChannel.class:NioServerSocketChannel.class)
                // 地址复用
                .option(ChannelOption.SO_REUSEADDR, serverBean.isReuseaddr())
                // Socket参数，服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。
                .option(ChannelOption.SO_BACKLOG, serverBean.getBacklog())

                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_RCVBUF, serverBean.getRevbuf())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline =   ch.pipeline();
                        intProtocolHandler(channelPipeline,serverBean);
                        channelPipeline.addLast(new IdleStateHandler(serverBean.getHeart(),0,0))
                        .addLast( ServerAutoConfigure.getBean(serverBean.getHandler()));
                    }
                })
                // 立即发送
                .childOption(ChannelOption.TCP_NODELAY, serverBean.isTcpNodelay())
                // Socket参数，连接保活，默认值为False。启用该功能时，TCP会主动探测空闲连接的有效性。可以将此功能视为TCP的心跳机制，需要注意的是：默认的心跳间隔是7200s即2小时。Netty默认关闭该功能
                .childOption(ChannelOption.SO_KEEPALIVE, serverBean.isKeepalive())
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        bootstrap.bind(IpUtils.getHost(),serverBean.getPort()).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()){
                log.info("服务端启动成功【" + IpUtils.getHost() + ":" + serverBean.getPort() + "】");
            }
            else{
                log.info("服务端启动失败【" + IpUtils.getHost() + ":" + serverBean.getPort() + "】");
            }
        });
    }



    private  void intProtocolHandler(ChannelPipeline channelPipeline, NettyProperties serverBean){
        Class<MessageToMessageDecoder>[] decoders =   serverBean.getDecoders();
        Class<MessageToMessageEncoder>[] encoders = serverBean.getEncoders();
        if(decoders!=null&&decoders.length>0){
            for(Class<MessageToMessageDecoder> clazz: decoders){
                try {
                    channelPipeline.addLast(clazz.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
         if(encoders!=null&&encoders.length>0){
                for( Class<MessageToMessageEncoder> clazz : encoders){
                    try {
                        channelPipeline.addLast(clazz.newInstance());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
         }
        }


    }

    /**
     * 初始化EnentPool 参数
     */

    private void  initEventPool(){
        bootstrap= new ServerBootstrap();
        if(useEpoll()){
            bossGroup = new EpollEventLoopGroup(serverBean.getBossThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "LINUX_BOSS_" + index.incrementAndGet());
                }
            });
            workGroup = new EpollEventLoopGroup(serverBean.getWorkThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "LINUX_WORK_" + index.incrementAndGet());
                }
            });

        }
        else {
            bossGroup = new NioEventLoopGroup(serverBean.getBossThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "BOSS_" + index.incrementAndGet());
                }
            });
            workGroup = new NioEventLoopGroup(serverBean.getWorkThread(), new ThreadFactory() {
                private AtomicInteger index = new AtomicInteger(0);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "WORK_" + index.incrementAndGet());
                }
            });
        }
    }

    /**
     * 关闭资源
     */
    @Override
    public void shutdown() {
        if(workGroup!=null && bossGroup!=null ){
            try {
                bossGroup.shutdownGracefully().sync();// 优雅关闭
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                log.info("服务端关闭资源失败【" + IpUtils.getHost() + ":" + serverBean.getPort() + "】");
            }
        }
    }

    private boolean useEpoll() {
        return RemotingUtil.isLinuxPlatform()
                && Epoll.isAvailable();
    }
}

