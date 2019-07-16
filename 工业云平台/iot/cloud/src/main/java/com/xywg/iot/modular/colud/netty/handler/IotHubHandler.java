package com.xywg.iot.modular.colud.netty.handler;

import cn.hutool.json.JSONUtil;
import com.xywg.iot.dto.DataDTO;
import com.xywg.iot.dto.XySession;
import com.xywg.iot.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:43 2018/12/11
 * Modified By : wangyifei
 */
public class IotHubHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final String loginJson;
    private final String clientId;

    private final NettyUtil nettyUtil;

    public IotHubHandler(String loginJson, String clientId, NettyUtil nettyUtil) {
        this.loginJson = loginJson;
        this.clientId = clientId;
        this.nettyUtil = nettyUtil;
    }

    /**
     * 绑定通道
     */
    public static final AttributeKey<XySession> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");
    /**
     * 本地通道缓存
     */
    public static Map<String, Channel> nettyChannels = new ConcurrentHashMap<>(10);
    private static final Logger LOGGER = LoggerFactory.getLogger(IotHubHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        int len = in.readableBytes();
        //  可读开始位置
        byte[] msgData = new byte[len];
        in.readBytes(msgData);
        String data = new String(msgData);
        DataDTO dataDTO = JSONUtil.toBean(data, DataDTO.class);
        String[] method = dataDTO.getMethod().split("\\.");
        if ("login".equals(method[0])) {
            String token = dataDTO.getParams().get("token");
            if (StringUtils.isNotBlank(token)) {
                XySession xySession = ctx.channel().attr(NETTY_CHANNEL_KEY).get();
                xySession.setToken(token);
                ctx.channel().attr(NETTY_CHANNEL_KEY).set(xySession);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        nettyChannels.put(clientId, ctx.channel());
        XySession xySession = new XySession();
        xySession.setId(clientId);
        //xySession.setId(IdUtil.simpleUUID());
        ctx.channel().attr(NETTY_CHANNEL_KEY).set(xySession);
        LOGGER.info("###########已连接服务器############");
        LOGGER.info("###########发送登录############");
        nettyUtil.send(loginJson, ctx.channel(), false);

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
        LOGGER.error("异常[{}]", cause.getMessage());
        ctx.close();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        nettyChannels.remove(ctx.channel().attr(NETTY_CHANNEL_KEY).get().getId());
        ctx.channel().closeFuture();
        LOGGER.info("###########有设备断开处理器############");
    }


}
