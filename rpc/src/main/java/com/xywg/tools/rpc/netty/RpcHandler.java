package com.xywg.tools.rpc.netty;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xywg.tools.rpc.netty.common.Const;
import com.xywg.tools.rpc.netty.common.Protocol;
import com.xywg.tools.rpc.netty.config.RpcConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.stereotype.Component;

/**
 * @author : wangyifei
 * Description  监听本地端口，并转发到相应端口
 * Date: Created in 9:43 2018/12/11
 * Modified By : wangyifei
 */
@Component
@ChannelHandler.Sharable
public class RpcHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 确定客户端应用名
     * @param ctx
     * @param protocol
     */
    private void updSession(ChannelHandlerContext ctx,Protocol protocol){
        XySession xySession  = ctx.channel().attr(Const.NETTY_CHANNEL_KEY).get();
            xySession.setAppName(protocol.getAppName());
            ctx.channel().attr(Const.NETTY_CHANNEL_KEY).set(xySession);
            Const.clientChannels.put(protocol.getAppName(),ctx.channel());
    }

    /**
     * 登录方法
     * @param protocol
     * @return
     */
    private  boolean isloginMethod(Protocol protocol){
        if ("login".equals(protocol.getMethod())){
            return true;
        }
        return false;
    }

    /**
     * 是否登录
     * @return
     */
    private boolean isLogin(ChannelHandlerContext ctx){
        XySession xySession = ctx.channel().attr(Const.NETTY_CHANNEL_KEY).get();
        if(StrUtil.isNotBlank(xySession.getAppName())){
            return true;
        }
        return false;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        int len = in.readableBytes();
        //  可读开始位置
        byte[] msgData = new byte[len];
        in.readBytes(msgData);
        ByteBuf byteBuf = Unpooled.buffer(len);
        byteBuf.writeBytes(msgData);
        Protocol protocol = JSONUtil.toBean(new String(msgData),Protocol.class);
        if (isloginMethod(protocol)){
            updSession(ctx,protocol);
            return;
        }
        if(!isLogin(ctx)){
            throw  new RuntimeException("未登录");
        }
        Channel toClientChannel =Const.clientChannels.get(protocol.getToAppName());
        if(toClientChannel==null){
            protocol.setStatus("2");
            protocol.setParams("no target");
            String jsonstr = JSONUtil.toJsonStr(protocol);
            ByteBuf byteBuf1 = Unpooled.buffer(jsonstr.length());
            byteBuf1.writeBytes(jsonstr.getBytes());
            ctx.channel().writeAndFlush(byteBuf1);
        }else{
            toClientChannel.writeAndFlush(byteBuf);
        }


    }

    /**
     * 建立连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 初始化通道信息
        XySession xySession =XySession.factory(RandomUtil.randomString(16));
        ctx.channel().attr(Const.NETTY_CHANNEL_KEY).set(xySession);



    }

    /**
     * 事件变化
     * @param ctx
     * @param evt
     * @throws Exception
     */

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                ctx.close();
            }
        }
    }

    /**
     * 异常
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.channel().close();
    }

    /**
     * 断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String appName = ctx.channel().attr(Const.NETTY_CHANNEL_KEY).get().getAppName();
        if(StrUtil.isNotBlank(appName)){
            Const.clientChannels.remove(appName);
        }


    }


}
