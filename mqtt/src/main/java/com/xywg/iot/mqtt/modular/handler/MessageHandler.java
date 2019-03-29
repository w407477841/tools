package com.xywg.iot.mqtt.modular.handler;

import com.wyf.iot.handler.BaseHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:28 2019/3/26
 * Modified By : wangyifei
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageHandler extends BaseHandler {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("哈哈");
    }
}
