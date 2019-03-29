package com.xywg.tools.tcpforward.netty.server;


import cn.hutool.core.util.RandomUtil;
import com.xywg.tools.tcpforward.netty.client.ClientConfig;
import com.xywg.tools.tcpforward.netty.client.thread.impl.SimpleClientThread;
import com.xywg.tools.tcpforward.netty.common.Const;
import com.xywg.tools.tcpforward.netty.common.XySession;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description  监听本地端口，并转发到相应端口
 * Date: Created in 9:43 2018/12/11
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class AccetperHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Autowired
    private ClientConfig clientConfig ;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println("服务端收到数据");
         while(!ctx.channel().attr(Const.NETTY_CHANNEL_KEY).get().isOk()){
             try {
                 System.out.println("等待 通道开启");
                 Thread.sleep(500);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
        System.out.println("通过客户端转发数据");
        System.out.println( "16进制形式："+  ByteBufUtil.hexDump(in).toUpperCase());
        int len = in.readableBytes();
        //  可读开始位置
        byte[] msgData = new byte[len];
        in.readBytes(msgData);
        System.out.println( "字符形式："+  new String(msgData));

        ByteBuf byteBuf = Unpooled.buffer(len);
        byteBuf.writeBytes(msgData);

        Const.clientChannels.get(ctx.channel().attr(Const.NETTY_CHANNEL_KEY).get().getClientId()).writeAndFlush(byteBuf);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
         // 生成serverId ,clientId
        XySession xySession = new XySession(RandomUtil.randomString(16),RandomUtil.randomString(16));
        ctx.channel().attr(Const.NETTY_CHANNEL_KEY).set(xySession);
        // 保存 服务端通道
        Const.serverChannels.put(xySession.getServerId(),ctx.channel());

        // 启动客户端
        Thread t = new Thread(new SimpleClientThread(clientConfig, xySession.getServerId(), xySession.getClientId()));
        t.start();


    }



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                ctx.close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        System.out.println("服务端出现异常");
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        XySession xySession = ctx.channel().attr(Const.NETTY_CHANNEL_KEY).get();
        Const.serverChannels.remove(xySession.getServerId());
        Const.clientChannels.get(xySession.getClientId()).close();
    }


}
