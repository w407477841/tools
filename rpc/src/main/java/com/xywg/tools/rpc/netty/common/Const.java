package com.xywg.tools.rpc.netty.common;

import com.xywg.tools.rpc.netty.XySession;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:34 2019/2/20
 * Modified By : wangyifei
 */
public class Const {

    /**
     * 绑定通道
     */
    public static final AttributeKey<XySession> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");

    /**
     * 客户端通道
     * key = clientId
     */
    public static Map<String, Channel> clientChannels = new ConcurrentHashMap<>();

    public static  final String HEARTBEAT = "1" ;


}
