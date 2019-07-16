package com.xywg.iot.common.service;

import com.alibaba.fastjson.JSON;
import com.xywg.iot.dto.XySession;
import com.xywg.iot.util.DateUtils;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hjy
 * @date 2019/1/23
 */
@Service
public class WebsocketSendMessageService {
    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;

    private static final AttributeKey<XySession> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");

    public  void sendWebsocket(String msg, Channel channel) {
        if (channel == null) {
            return;
        }
        XySession xySession = channel.attr(NETTY_CHANNEL_KEY).get();
        if (xySession != null) {
            if (xySession.getPk() == null || xySession.getDn() == null) {
                return;
            }
            String topic = "/debug/" + xySession.getPk() + "/" + xySession.getDn();
            //将服务器回复的数据往WebSocket 推送一份(用于在线调试)
            sendMessage(topic, "服务器下发", msg);
        }
    }

    /**
     * 推送到websocket
     *
     * @param dataMode 可选(数据推送的模式:服务器下发,设备上报)
     * @param data     发送的内容
     * @param topic    主题
     */
    public void sendMessage(String topic, String dataMode, String data) {
        if (StringUtils.isBlank(dataMode)) {
            dataMode = "";
        }
        Map<String, Object> webSocketMap = new HashMap<>(2);
        webSocketMap.put("date", DateUtils.getDateTime());
        webSocketMap.put("data", data);
        webSocketMap.put("dataMode", dataMode);
        simpMessageSendingOperations.convertAndSend(topic, JSON.toJSONString(webSocketMap));
    }



}
