package com.xywg.iot.mqtt.modular.handler;

import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.wyf.iot.common.Const;
import com.xywg.iot.mqtt.modular.response.Response;
import io.netty.buffer.ByteBuf;
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
public class MessageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest in) throws Exception {
           ByteBuf content =  in.content();
           String request =  Const.bytebuf2ascii(content);
           System.out.println(request);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
            Response rsp = new Response();
            rsp.setCode(200);
            rsp.setData("hello world");
            rsp.setMessage("ok");
            String json = JSONUtil.toJsonStr(rsp);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,json.length()).set(HttpHeaderNames.DATE,new Date()).set(HttpHeaderNames.CONTENT_TYPE,ContentType.JSON);
            response.content().writeBytes(json.getBytes());
            ctx.writeAndFlush(response);

    }
}
