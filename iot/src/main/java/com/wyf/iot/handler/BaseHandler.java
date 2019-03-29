package com.wyf.iot.handler;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:43 2018/12/11
 * Modified By : wangyifei
 */
@Slf4j
public abstract class BaseHandler extends  SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("【channelInactive】"+ctx.channel().localAddress().toString()+"关闭成功");

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("【异常】" + ctx.channel().remoteAddress() + "【channel 关闭】",cause);
        ctx.close();

    }



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {


        if(evt instanceof IdleStateEvent){
            log.info("【心跳超时】" + ctx.channel().remoteAddress() + "【channel 关闭】");
            switch (((IdleStateEvent) evt).state()) {
                case READER_IDLE:
                    ctx.close();
                case WRITER_IDLE:
                    ctx.close();
                case ALL_IDLE:
                    ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
