package com.xywg.tools.simulator.netty.client;

import com.xywg.tools.simulator.netty.common.Const;
import com.xywg.tools.simulator.netty.common.XySession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:43 2018/12/11
 * Modified By : wangyifei
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {


    private final String clientId;


    public ClientHandler(String clientId) {
        this.clientId = clientId;
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
//        int len = in.readableBytes();
//        //  可读开始位置
//        byte[] msgData = new byte[len];
//        in.readBytes(msgData);
//        ByteBuf byteBuf = Unpooled.buffer(len);
//        byteBuf.writeBytes(msgData);
//        throw  new RuntimeException("故意的");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        XySession xySession  =new XySession(clientId);
        xySession.setOk(true);
        ctx.channel().attr(Const.NETTY_CHANNEL_KEY).set(xySession);
        // 保存客户端通道
        Const.clientChannels.put(clientId,ctx.channel());





    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.info("###客户端闲置 主动关闭连接###");
                ctx.close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        System.out.println("客户端出现异常");
        LOGGER.error("异常[{}]", cause.getMessage());
        ctx.close();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Const.clientChannels.remove(clientId);
        ctx.channel().closeFuture();
        LOGGER.info("###########有设备断开处理器############");
    }


}
