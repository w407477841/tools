package com.xywg.iot.mqtt.modular.handler;

import cn.hutool.http.ContentType;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:28 2019/3/26
 * Modified By : wangyifei
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<HttpRequest> {
    private final  Pattern pattern  = Pattern.compile("\r|\n");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest in) throws Exception {
        System.out.println("in");
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
            StringBuffer sb = new StringBuffer();
            sb.append("{\"message\":\"ok\"}");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,sb.length()).set(HttpHeaderNames.DATE,new Date()).set(HttpHeaderNames.CONTENT_TYPE,ContentType.JSON);
            response.content().writeBytes(sb.toString().getBytes());
            ctx.writeAndFlush(response);

    }
}
