package websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:04 2019/5/27
 * Modified By : wangyifei
 */
public class WebsocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof FullHttpRequest){
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }else if(msg instanceof WebSocketFrame){
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,FullHttpRequest req){
                if(!req.decoderResult().isSuccess()||("websocket".equals(req.headers().get("Upgrade")))){
                        //TODO sendHttpresponse
                    return ;
                }
        WebSocketServerHandshakerFactory wsfactory  =  new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket",null,false);
        handshaker = wsfactory.newHandshaker(req);
        if(handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(),req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx,WebSocketFrame frame){
                //关闭链路指令
            if(frame instanceof CloseWebSocketFrame){
                handshaker.close(ctx.channel(),((CloseWebSocketFrame) frame).retain());
                return ;
            }
                //ping 消息
            if(frame instanceof PingWebSocketFrame){
                ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
                return ;
            }

            if(!(frame instanceof  TextWebSocketFrame)){
                throw new UnsupportedOperationException(String.format("%s frame types not supported",frame.getClass().getName()));
            }
            String request =  ((TextWebSocketFrame) frame).text();


    }
}
