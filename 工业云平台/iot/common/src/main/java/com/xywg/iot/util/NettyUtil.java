package com.xywg.iot.util;

import cn.hutool.json.JSONUtil;
import com.xywg.iot.common.service.WebsocketSendMessageService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:18 2018/12/14
 * Modified By : wangyifei
 */
@Component
public class NettyUtil {
    @Autowired
    private WebsocketSendMessageService websocketSendMessageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyUtil.class);


    public void send(Object msg, Channel channel, boolean isWebsocketPush) {
        String json = JSONUtil.toJsonStr(msg).replaceAll("\"", "");
        ByteBuf byteBuf = Unpooled.buffer(json.length());
        byteBuf.writeBytes(json.getBytes());
        channel.writeAndFlush(byteBuf);
        //推送到websocket
        if(isWebsocketPush){
            websocketSendMessageService.sendWebsocket(json, channel);
        }

    }



    public void send(String msg, Channel channel, boolean isWebsocketPush) {
        ByteBuf byteBuf = Unpooled.buffer(msg.length());
        byteBuf.writeBytes(msg.getBytes());
        channel.writeAndFlush(byteBuf);

        //推送到websocket
        if(isWebsocketPush){
            websocketSendMessageService.sendWebsocket(msg, channel);
        }
    }


    /**
     * @param pk 产品PK
     * @param dn 设备名
     * @return
     */
    public static String localKey(String pk, String dn) {
        return pk + "." + dn;
    }

}
