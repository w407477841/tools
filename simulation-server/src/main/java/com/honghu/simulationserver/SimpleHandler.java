package com.honghu.simulationserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:33 2019/3/29
 * Modified By : wangyifei
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class SimpleHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.wyf.iot.handler.SimpleHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String o) throws Exception {
            log.info("收到数据[{}]",o);
    }
}
